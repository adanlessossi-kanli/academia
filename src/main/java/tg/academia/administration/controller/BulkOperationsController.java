package tg.academia.administration.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tg.academia.administration.service.BulkOperationsService;

import java.util.Map;

@RestController
@RequestMapping("/api/bulk")
@RequiredArgsConstructor
public class BulkOperationsController {

    private final BulkOperationsService bulkOperationsService;

    @PostMapping("/students/import")
    @Operation(summary = "Import students from CSV")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> importStudents(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = bulkOperationsService.importStudents(file);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/students/export")
    @Operation(summary = "Export students to CSV")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<byte[]> exportStudents() {
        byte[] csvData = bulkOperationsService.exportStudents();
        return ResponseEntity.ok()
                .header("Content-Type", "text/csv")
                .header("Content-Disposition", "attachment; filename=students.csv")
                .body(csvData);
    }

    @PostMapping("/attendance/mark-bulk")
    @Operation(summary = "Mark attendance for multiple students")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<Map<String, Object>> markBulkAttendance(@RequestBody BulkAttendanceRequest request) {
        Map<String, Object> result = bulkOperationsService.markBulkAttendance(request);
        return ResponseEntity.ok(result);
    }

    public record BulkAttendanceRequest(
            java.util.List<Long> studentIds,
            String date,
            String status
    ) {}
}