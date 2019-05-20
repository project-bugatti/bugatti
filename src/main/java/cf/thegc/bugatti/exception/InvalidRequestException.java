package cf.thegc.bugatti.exception;

public class InvalidRequestException extends RuntimeException {

    String message;

    public InvalidRequestException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
