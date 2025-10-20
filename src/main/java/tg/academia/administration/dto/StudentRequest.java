package tg.academia.administration.dto;

import jakarta.validation.constraints.*;

public record StudentRequest(
    @NotBlank(message = "First name is required")
    String firstName,
    
    @NotBlank(message = "Last name is required")
    String lastName,
    
    @Min(value = 1, message = "Grade must be between 1 and 6")
    @Max(value = 6, message = "Grade must be between 1 and 6")
    Integer grade,
    
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    String email,
    
    Long classId
) {}
