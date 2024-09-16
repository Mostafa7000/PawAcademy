package pawacademy.solution.question.domain;

import lombok.ToString;
import pawacademy.ResponseException;

@ToString
public enum QuestionType {
    MULTIPLE_CHOICE,
    TRUE_FALSE;

    public static QuestionType get(String type) throws ResponseException {
        if (type.equals("MULTIPLE_CHOICE")) {
            return MULTIPLE_CHOICE;
        } else if (type.equals("TRUE_FALSE")) {
            return TRUE_FALSE;
        } else {
            throw new ResponseException("Invalid enum value");
        }
    }
}
