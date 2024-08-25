package pawacademy;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ResponseException extends Exception {
    private String message;
    private int statusCode = 400;

    public ResponseException(String message) {
        this.message = message;
    }

    public ResponseException(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
