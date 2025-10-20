package tg.academia.administration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tg.academia.administration.dto.PageResponse;
import tg.academia.administration.dto.StudentRequest;
import tg.academia.administration.entity.Student;
import tg.academia.administration.exception.ResourceNotFoundException;
import tg.academia.administration.repository.StudentRepository;
import tg.academia.administration.service.StudentService;

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
    public PageResponse<Student> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "lastName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        var sort = "desc".equalsIgnoreCase(sortDir) 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        
        var pageable = PageRequest.of(page, size, sort);
        var studentsPage = studentRepository.findAll(pageable);
        
        return new PageResponse<>(studentsPage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Student getStudentById(@PathVariable Long id) {
        return studentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student", id));
    }

    @GetMapping("/grade/{grade}")
    @Operation(summary = "Get students by grade")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public List<Student> getStudentsByGrade(@PathVariable Integer grade) {
        return studentService.getStudentsByGrade(grade);
    }

    @GetMapping("/search")
    @Operation(summary = "Search students by name")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public PageResponse<Student> searchStudents(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        var pageable = PageRequest.of(page, size);
        var studentsPage = studentRepository.searchByNamePageable(name, pageable);
        return new PageResponse<>(studentsPage);
    }
    
    @GetMapping("/search/all")
    @Operation(summary = "Search all students by name (non-paginated)")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public List<Student> searchAllStudents(@RequestParam String name) {
        return studentRepository.searchByName(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new student")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Student createStudent(@Valid @RequestBody StudentRequest request) {
        return studentService.createStudent(
            request.firstName(), 
            request.lastName(), 
            request.grade(), 
            request.email(), 
            request.classId()
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update student")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Student updateStudent(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
        return studentService.updateStudent(
            id, 
            request.firstName(), 
            request.lastName(), 
            request.grade(), 
            request.email(), 
            request.classId()
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete student")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }
}
