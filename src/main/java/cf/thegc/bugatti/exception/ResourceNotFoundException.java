package cf.thegc.bugatti.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {

    private String message;
    private HttpStatus status;

    public ResourceNotFoundException(UUID culprit) {
        this("Resource", culprit);
    }

    public ResourceNotFoundException(String resource, UUID culprit) {
        this.message = resource + " with ID '" + culprit + "' does not exist";
        this.status = HttpStatus.NOT_FOUND;

        /*
        Resource with ID 'a059061a-6dce-11e9-a923-1681be663d3e' does not exist
         */
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
