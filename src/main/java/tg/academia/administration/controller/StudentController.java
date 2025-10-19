package tg.academia.administration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tg.academia.administration.dto.PageResponse;
import tg.academia.administration.entity.Student;
import tg.academia.administration.exception.ResourceNotFoundException;
import tg.academia.administration.repository.StudentRepository;
import tg.academia.administration.service.StudentService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Tag(name = "Students", description = "Student management operations")
public class StudentController {
    
    private final StudentRepository studentRepository;
    private final StudentService studentService;

    @GetMapping
    @Operation(summary = "Get all students with pagination")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<PageResponse<Student>> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Student> students = studentRepository.findAll(pageable);
        
        return ResponseEntity.ok(new PageResponse<>(students));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Student student = studentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student", id));
        return ResponseEntity.ok(student);
    }

    @GetMapping("/grade/{grade}")
    @Operation(summary = "Get students by grade")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<Student>> getStudentsByGrade(@PathVariable Integer grade) {
        return ResponseEntity.ok(studentRepository.findByGrade(grade));
    }

    @GetMapping("/search")
    @Operation(summary = "Search students by name")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<Student>> searchStudents(@RequestParam String name) {
        return ResponseEntity.ok(studentRepository.searchByName(name));
    }

    @PostMapping
    @Operation(summary = "Create new student")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<Student> createStudent(@Valid @RequestBody CreateStudentRequest request) {
        Student student = studentService.createStudent(
            request.firstName(), 
            request.lastName(), 
            request.grade(), 
            request.email(), 
            request.classId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update student")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @Valid @RequestBody CreateStudentRequest request) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student", id);
        }
        Student student = studentService.updateStudent(id, request.firstName(), request.lastName(), 
            request.grade(), request.email(), request.classId());
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete student")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student", id);
        }
        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public record CreateStudentRequest(
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
}
