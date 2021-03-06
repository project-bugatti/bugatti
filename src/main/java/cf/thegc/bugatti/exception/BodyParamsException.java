package cf.thegc.bugatti.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BodyParamsException extends RuntimeException {

    public final static String MISSING_PARAMS = "Missing required body parameter(s)";
    public final static String INVALID_FILETYPE = "Invalid file type";

    public final static String MEMBER_OBJECT_MISSING = "Member object missing";
    public final static String MEDIA_OBJECT_MISSING = "Media object missing";
    public final static String QUOTE_OBJECT_MISSING = "Quote object missing";
    public final static String MISSING_MEMBER_ID = "Missing Member ID";
    public final static String MISSING_MEDIA_ID = "Missing Media ID";

    public BodyParamsException(String message) {
        super(message);
    }
}
