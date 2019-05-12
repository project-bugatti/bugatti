package cf.thegc.bugatti.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BodyParamsException extends RuntimeException {

    public final static String MISSING_PARAMS = "Missing required body parameter(s)";
    public final static String INVALID_FILETYPE = "Invalid file type";

    public BodyParamsException(String message) {
        super(message);
    }
}
