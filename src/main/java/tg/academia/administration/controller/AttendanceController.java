package tg.academia.administration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tg.academia.administration.dto.AttendanceRequest;
import tg.academia.administration.entity.Attendance;
import tg.academia.administration.exception.ResourceNotFoundException;
import tg.academia.administration.repository.AttendanceRepository;
import tg.academia.administration.repository.StudentRepository;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@Tag(name = "Attendance", description = "Attendance management operations")
public class AttendanceController {
    
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    @GetMapping("/date/{date}")
    @Operation(summary = "Get attendance by date")
    public List<Attendance> getAttendanceByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return attendanceRepository.findByDate(date);
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get student attendance history")
    public List<Attendance> getStudentAttendance(
            @PathVariable Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return attendanceRepository.findByStudentIdAndDateBetween(studentId, startDate, endDate);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Mark attendance")
    public Attendance markAttendance(@Valid @RequestBody AttendanceRequest request) {
        var student = studentRepository.findById(request.studentId())
            .orElseThrow(() -> new ResourceNotFoundException("Student", request.studentId()));
        
        var attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setDate(request.date());
        attendance.setStatus(request.status());
        
        return attendanceRepository.save(attendance);
    }
}
