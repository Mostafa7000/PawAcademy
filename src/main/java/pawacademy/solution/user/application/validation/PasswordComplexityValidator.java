package pawacademy.solution.user.application.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordComplexityValidator implements ConstraintValidator<PasswordComplexity, String> {

    @Override
    public void initialize(PasswordComplexity constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }
        boolean hasDigit = false;
        boolean hasLetter = false;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (Character.isLetter(c)) {
                hasLetter = true;
            }
            if (hasDigit && hasLetter) {
                return true;
            }
        }
        return false;
    }
}