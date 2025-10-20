package tg.academia.administration.dto;

import java.time.LocalDateTime;

public record StudentResponse(
    Long id,
    String firstName,
    String lastName,
    String fullName,
    Integer grade,
    String email,
    Long classId,
    String className,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
