package xa.sh.ecom.ecom.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public class ErrorResponseDto {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    // Constructor, Getters, Setters...
    public ErrorResponseDto(HttpStatus status, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.path = path;
    }
    // Getters...
}