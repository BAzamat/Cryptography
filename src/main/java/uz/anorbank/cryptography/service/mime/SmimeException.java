package uz.anorbank.cryptography.service.mime;

public class SmimeException extends RuntimeException {
    private static final long serialVersionUID = 5400625787171945502L;

    public SmimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmimeException(String message) {
        super(message);
    }

    public SmimeException(Throwable cause) {
        super(cause);
    }
}
