package cf.thegc.bugatti.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MemberNotFoundException extends RuntimeException {

    private String message;

    public MemberNotFoundException(UUID culprit) {
        this.message = "Member with ID '" + culprit + "' does not exist.";
        /*
        Member with ID 'a059061a-6dce-11e9-a923-1681be663d3e' does not exist
         */
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
