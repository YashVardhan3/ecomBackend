package xa.sh.ecom.ecom.exception;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {
    private int status;
    private LocalDateTime timestamp;
    private String message;
    private List<String> details;

    // Constructors
    public ErrorResponse(int status, LocalDateTime timestamp, String message) {
        this.status = status;
        this.timestamp = timestamp;
        this.message = message;
    }

    public ErrorResponse(int status, LocalDateTime timestamp, String message, List<String> details) {
        this(status, timestamp, message);
        this.details = details;
    }

    // Getters

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}