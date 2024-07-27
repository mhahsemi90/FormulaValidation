package statement;

public class HandledError extends RuntimeException {
    public HandledError(String message) {
        super(message);
    }
}
