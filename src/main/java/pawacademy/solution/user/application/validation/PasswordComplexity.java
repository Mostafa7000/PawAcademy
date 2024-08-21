package pawacademy.solution.user.application.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordComplexityValidator.class)
public @interface PasswordComplexity {
    String message() default "Password must contain at least one digit and one character";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}