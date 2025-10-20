package tg.academia.administration.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

/**
 * Modern record-based pagination request with validation.
 */
public record PageRequest(
    @Min(value = 0, message = "Page number must be non-negative")
    int page,
    
    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 100, message = "Page size cannot exceed 100")
    int size,
    
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]*$", message = "Sort field must be a valid property name")
    String sortBy,
    
    @Pattern(regexp = "^(asc|desc)$", message = "Sort direction must be 'asc' or 'desc'")
    String sortDir
) {
    public PageRequest {
        // Compact constructor for validation
        if (page < 0) page = 0;
        if (size < 1) size = 10;
        if (size > 100) size = 100;
        if (sortBy == null || sortBy.isBlank()) sortBy = "id";
        if (sortDir == null || (!sortDir.equals("asc") && !sortDir.equals("desc"))) sortDir = "asc";
    }
    
    public static PageRequest of(int page, int size) {
        return new PageRequest(page, size, "id", "asc");
    }
    
    public static PageRequest of(int page, int size, String sortBy) {
        return new PageRequest(page, size, sortBy, "asc");
    }
    
    public static PageRequest defaultRequest() {
        return new PageRequest(0, 10, "id", "asc");
    }
}