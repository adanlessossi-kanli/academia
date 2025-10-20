package tg.academia.administration.dto;

import jakarta.validation.constraints.*;
import tg.academia.administration.entity.Attendance.AttendanceStatus;

import java.time.LocalDate;

public record AttendanceRequest(
    @NotNull(message = "Student ID is required")
    Long studentId,
    
    @NotNull(message = "Date is required")
    LocalDate date,
    
    @NotNull(message = "Status is required")
    AttendanceStatus status
) {}
