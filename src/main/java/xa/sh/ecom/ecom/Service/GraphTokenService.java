package xa.sh.ecom.ecom.Service;

import java.time.Duration; // For block timeout
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Service
public class GraphTokenService {

    private static final Logger log = LoggerFactory.getLogger(GraphTokenService.class);

    @Value("${graph.client-id}")
    private String clientId;

    @Value("${graph.client-secret}")
    private String clientSecret;

    @Value("${graph.tenant-id}")
    private String tenantId;

    private volatile String cachedToken; // volatile for visibility
    private volatile Instant tokenExpiry; // volatile for visibility

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Duration tokenRequestTimeout = Duration.ofSeconds(10); // Configurable timeout

    // Inject WebClient.Builder to leverage Spring Boot's auto-configuration
    public GraphTokenService(WebClient.Builder webClientBuilder,
                              @Value("${graph.tenant-id}") String tenantId) { // Inject tenantId for base URL
        String baseUrl = "https://login.microsoftonline.com/" + tenantId;
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
        log.info("GraphTokenService initialized with WebClient, base URL: {}", baseUrl);
    }

    // Still synchronized for the check-then-act logic on the cache
    public synchronized String getAccessToken() {
        if (cachedToken == null || tokenExpiry == null || Instant.now().isAfter(tokenExpiry)) {
            log.info("Cached token is null or expired. Fetching new Graph API token via WebClient...");
            try {
                // Block ONLY if we absolutely need a synchronous return type here.
                // Add a timeout to prevent indefinite blocking.
                fetchNewToken().block(tokenRequestTimeout);
                log.info("New token fetched successfully via WebClient.");
            } catch (Exception e) {
                log.error("Failed to fetch new Graph API token using WebClient", e);
                // Clear cache on failure
                cachedToken = null;
                tokenExpiry = null;
                // Re-throw as a runtime exception to signal failure
                throw new RuntimeException("Failed to obtain Graph API token", e);
            }
        } else {
            log.debug("Using cached Graph API token.");
        }
        // Check if token was successfully populated after blocking
        if (cachedToken == null) {
             log.error("Token fetch failed, returning null or throwing exception.");
             // Decide whether to return null or throw based on requirements
             throw new RuntimeException("Failed to obtain Graph API token after attempting fetch.");
        }
        System.out.println("cached token :" + cachedToken);
        return cachedToken;
        
    }

    // Returns a Mono, making it suitable for reactive pipelines if needed elsewhere
    // Returns Mono<Void> because its primary effect is updating the cached fields
    private Mono<Void> fetchNewToken() {
        String path = "/oauth2/v2.0/token"; // Path relative to base URL

        log.debug("Executing POST request to {}", webClient.mutate().build().post().uri(path).toString()); // Log effective URL

        return this.webClient.post()
                .uri(path)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("scope", "https://graph.microsoft.com/.default")
                        .with("grant_type", "client_credentials"))
                .retrieve() // Checks for 4xx/5xx errors and throws WebClientResponseException
                .bodyToMono(String.class) // Get the response body as a String Mono
                .flatMap(responseBody -> { // Use flatMap for operations that might fail or are async
                    try {
                        log.debug("Received token response body: {}", responseBody);
                        JsonNode jsonNode = objectMapper.readTree(responseBody);
                        if (jsonNode.has("access_token") && jsonNode.has("expires_in")) {
                            String token = jsonNode.get("access_token").asText();
                            int expiresIn = jsonNode.get("expires_in").asInt();

                            // Update cache - the calling method (getAccessToken) is synchronized
                            cachedToken = token;
                            tokenExpiry = Instant.now().plusSeconds(expiresIn - 300); // 60 sec buffer
                            log.debug("Token parsed successfully. Expires at: {}", tokenExpiry);
                            return Mono.empty(); // Indicate success (complete Mono<Void>)
                        } else {
                            log.error("Failed to parse token response: missing access_token or expires_in. Body: {}", responseBody);
                            // Use Mono.error for controlled error propagation within the reactive chain
                            return Mono.error(new RuntimeException("OAuth token response missing required fields (access_token or expires_in)"));
                        }
                    } catch (Exception e) {
                         log.error("Failed to parse JSON token response body: {}", responseBody, e);
                         // Use Mono.error for controlled error propagation
                        return Mono.error(new RuntimeException("Failed to parse Graph API token response JSON", e));
                    }
                })
                .doOnError(error -> {
                    // This logs errors that occurred anywhere in the chain above
                    // (e.g., network errors, 4xx/5xx status, JSON parsing errors)
                    log.error("Error during WebClient token fetch processing: {}", error.getMessage());
                    // Cache clearing is handled in the synchronized getAccessToken method's catch block
                })
                .then(); // Convert the result Mono<Something> to Mono<Void> after processing
    }

    // No @PreDestroy needed for WebClient typically, connections are managed by Netty/Reactor Netty
}