package pawacademy;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

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
        var body = Response.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.FORBIDDEN.value())
                .message("You are blocked. Contact The Admin")
                .data(null)
                .build();

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        var cause = e.getCause();
        if (cause instanceof AuthorizationException) {
            var body = Response.builder()
                    .timestamp(LocalDateTime.now())
                    .statusCode(HttpStatus.FORBIDDEN.value())
                    .message("You are blocked. Contact The Admin")
                    .data(null)
                    .build();

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(body);
        }
        var body = Response.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .data(null)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }
}
