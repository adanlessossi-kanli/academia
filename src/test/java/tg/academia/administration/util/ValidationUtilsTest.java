package tg.academia.administration.util;

import org.junit.jupiter.api.Test;
import tg.academia.administration.exception.ValidationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValidationUtilsTest {

    @Test
    void validateGrade_ValidGrade_DoesNotThrow() {
        ValidationUtils.validateGrade(1);
        ValidationUtils.validateGrade(6);
        ValidationUtils.validateGrade(3);
    }

    @Test
    void validateGrade_InvalidGrade_ThrowsException() {
        assertThatThrownBy(() -> ValidationUtils.validateGrade(null))
                .isInstanceOf(ValidationException.class);
        assertThatThrownBy(() -> ValidationUtils.validateGrade(0))
                .isInstanceOf(ValidationException.class);
        assertThatThrownBy(() -> ValidationUtils.validateGrade(7))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void validateEmail_ValidEmail_DoesNotThrow() {
        ValidationUtils.validateEmail("test@example.com");
        ValidationUtils.validateEmail("user.name+tag@domain.co.uk");
    }

    @Test
    void validateEmail_InvalidEmail_ThrowsException() {
        assertThatThrownBy(() -> ValidationUtils.validateEmail(null))
                .isInstanceOf(ValidationException.class);
        assertThatThrownBy(() -> ValidationUtils.validateEmail(""))
                .isInstanceOf(ValidationException.class);
        assertThatThrownBy(() -> ValidationUtils.validateEmail("invalid-email"))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void validateName_ValidName_DoesNotThrow() {
        ValidationUtils.validateName("John Doe", "firstName");
        ValidationUtils.validateName("Mary-Jane O'Connor", "lastName");
    }

    @Test
    void validateName_InvalidName_ThrowsException() {
        assertThatThrownBy(() -> ValidationUtils.validateName(null, "firstName"))
                .isInstanceOf(ValidationException.class);
        assertThatThrownBy(() -> ValidationUtils.validateName("", "firstName"))
                .isInstanceOf(ValidationException.class);
        assertThatThrownBy(() -> ValidationUtils.validateName("A", "firstName"))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void validationBuilder_ValidValue_ReturnsValue() {
        String result = ValidationUtils.validate("test", "field")
                .isNotNull()
                .isNotBlank()
                .get();
        
        assertThat(result).isEqualTo("test");
    }

    @Test
    void validationBuilder_NullValue_ThrowsException() {
        assertThatThrownBy(() -> ValidationUtils.validate(null, "field")
                .isNotNull()
                .throwIfInvalid())
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void validationBuilder_BlankString_ThrowsException() {
        assertThatThrownBy(() -> ValidationUtils.validate("", "field")
                .isNotBlank()
                .throwIfInvalid())
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void validationBuilder_FailsPredicate_ThrowsException() {
        assertThatThrownBy(() -> ValidationUtils.validate(5, "number")
                .matches(n -> n > 10, "Number must be greater than 10")
                .throwIfInvalid())
                .isInstanceOf(ValidationException.class);
    }
}