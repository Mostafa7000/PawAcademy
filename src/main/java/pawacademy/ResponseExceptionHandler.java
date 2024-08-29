package pawacademy;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
@ControllerAdvice
@RequiredArgsConstructor
public class ResponseExceptionHandler {
    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<?> handleException(ResponseException e) {
        System.out.println(e.getMessage());
        var body = Response.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(e.getStatusCode())
                .message(e.getMessage())
                .data(null)
                .build();

        return ResponseEntity
                .status(e.getStatusCode())
                .body(body);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<?> handleException(AuthorizationException e) {
        System.out.println(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(e.getMessage());
    }
}
