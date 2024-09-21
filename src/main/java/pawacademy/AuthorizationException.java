package pawacademy;

public class AuthorizationException extends ResponseException {
    public AuthorizationException(String message) {
        super(message, 403);
    }
}
