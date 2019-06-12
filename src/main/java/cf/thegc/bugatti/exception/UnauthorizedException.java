package cf.thegc.bugatti.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends RuntimeException {

    private String message;
    private String details;
    private HttpStatus status;

    public UnauthorizedException(String details) {
        this.message = "Unauthorized";
        this.details = details;
        this.status = HttpStatus.UNAUTHORIZED;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
