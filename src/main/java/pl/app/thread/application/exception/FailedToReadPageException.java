package pl.app.thread.application.exception;

public class FailedToReadPageException extends RuntimeException {
    public FailedToReadPageException() {
        super();
    }

    public FailedToReadPageException(String message) {
        super(message);
    }
}
