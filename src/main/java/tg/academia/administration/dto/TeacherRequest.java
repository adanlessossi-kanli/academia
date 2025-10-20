package tg.academia.administration.dto;

import jakarta.validation.constraints.*;

public record TeacherRequest(
    @NotBlank(message = "First name is required")
    String firstName,
    
    @NotBlank(message = "Last name is required")
    String lastName,
    
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    String email,
    
    @NotBlank(message = "Subject is required")
    String subject
) {}
