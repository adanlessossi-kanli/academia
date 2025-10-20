package tg.academia.administration.dto;

import jakarta.validation.constraints.*;

public record GradeRequest(
    @NotNull(message = "Student ID is required")
    Long studentId,
    
    @NotBlank(message = "Subject is required")
    String subject,
    
    @NotBlank(message = "Semester is required")
    String semester,
    
    @DecimalMin(value = "0.0", message = "Score must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Score must be between 0 and 100")
    @NotNull(message = "Score is required")
    Double score
) {}
