package tg.academia.administration.exception;

/**
 * Exception thrown when a requested resource is not found.
 */
public final class ResourceNotFoundException extends AcademiaException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " not found with id: " + id);
    }
}