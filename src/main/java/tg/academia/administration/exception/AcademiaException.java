package tg.academia.administration.exception;

/**
 * Base sealed class for all Academia application exceptions.
 * Uses modern Java sealed classes for better type safety and exhaustive handling.
 */
public sealed class AcademiaException extends RuntimeException 
    permits ResourceNotFoundException, DuplicateResourceException, ValidationException {
    
    protected AcademiaException(String message) {
        super(message);
    }
    
    protected AcademiaException(String message, Throwable cause) {
        super(message, cause);
    }
}