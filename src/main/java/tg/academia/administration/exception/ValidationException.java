package tg.academia.administration.exception;

import java.util.Map;

/**
 * Exception for validation errors with field-specific error messages.
 */
public final class ValidationException extends AcademiaException {
    
    private final Map<String, String> fieldErrors;
    
    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(message);
        this.fieldErrors = Map.copyOf(fieldErrors);
    }
    
    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}