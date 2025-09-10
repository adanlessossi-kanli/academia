package tg.academia.administration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tg.academia.administration.entity.Attendance;
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
    public List<Attendance> getAttendanceByDate(@PathVariable String date) {
        return attendanceRepository.findByDate(LocalDate.parse(date));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get student attendance history")
    public List<Attendance> getStudentAttendance(
            @PathVariable Long studentId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return attendanceRepository.findByStudentIdAndDateBetween(
            studentId, LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    @PostMapping
    @Operation(summary = "Mark attendance")
    public Attendance markAttendance(@RequestBody MarkAttendanceRequest request) {
        Attendance attendance = new Attendance();
        attendance.setStudent(studentRepository.findById(request.studentId()).orElseThrow());
        attendance.setDate(LocalDate.parse(request.date()));
        attendance.setStatus(Attendance.AttendanceStatus.valueOf(request.status()));
        return attendanceRepository.save(attendance);
    }

    public record MarkAttendanceRequest(
        Long studentId, 
        String date, 
        String status
    ) {}
}
