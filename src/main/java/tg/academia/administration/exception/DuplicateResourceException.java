package tg.academia.administration.exception;

/**
 * Exception thrown when attempting to create a resource that already exists.
 */
public final class DuplicateResourceException extends AcademiaException {
    
    public DuplicateResourceException(String message) {
        super(message);
    }
    
    public DuplicateResourceException(String resource, String field, String value) {
        super(resource + " already exists with " + field + ": " + value);
    }
}