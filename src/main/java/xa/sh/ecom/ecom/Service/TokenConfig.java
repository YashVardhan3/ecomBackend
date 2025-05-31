// package xa.sh.ecom.ecom.Service;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.reactive.function.client.WebClient;
// import org.springframework.web.reactive.function.client.support.WebClientAdapter;
// import org.springframework.web.service.invoker.HttpServiceProxyFactory;

// @Configuration
// public class TokenConfig {
//     @Value("${graph.tenant-id}")
//     private String tenantId;

//     String url = "https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0/token";
//     @Bean
//     WebClient webClient(){
//         return WebClient.create(url);
//     }
     

//     @Bean
//     HttpServiceProxyFactory httpServiceProxyFactory(final WebClient webClient){
//         return HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build();
//     }
// }
