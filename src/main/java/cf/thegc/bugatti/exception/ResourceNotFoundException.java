package cf.thegc.bugatti.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private String message;

    public ResourceNotFoundException(UUID culprit) {
        this(culprit, "Resource");
    }

    public ResourceNotFoundException(UUID culprit, String resource) {
        this.message = resource + " with ID '" + culprit + "' does not exist";

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
}
