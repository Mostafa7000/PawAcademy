package pawacademy;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class ResponseExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<?> handleException(ResponseException e) {
        System.out.println(e.getMessage());

        return ResponseEntity
                .status(e.getStatusCode())
                .body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericExceptions(Exception e) {
        System.out.println(e.getMessage());
        var message = messageSource.getMessage("generic.exception.message", new Object[]{}, LocaleContextHolder.getLocale());

        return ResponseEntity
                .status(400)
                .body(e.getMessage());
    }
}
