package tg.academia.administration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tg.academia.administration.entity.Student;
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
    @Operation(summary = "Get all students")
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("/grade/{grade}")
    @Operation(summary = "Get students by grade")
    public List<Student> getStudentsByGrade(@PathVariable Integer grade) {
        return studentRepository.findByGrade(grade);
    }

    @GetMapping("/search")
    @Operation(summary = "Search students by name")
    public List<Student> searchStudents(@RequestParam String name) {
        return studentRepository.searchByName(name);
    }

    @PostMapping
    @Operation(summary = "Create new student")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MANAGER')")
    public Student createStudent(@RequestBody CreateStudentRequest request) {
        return studentService.createStudent(
            request.firstName(), 
            request.lastName(), 
            request.grade(), 
            request.email(), 
            request.classId()
        );
    }

    public record CreateStudentRequest(
        String firstName, 
        String lastName, 
        Integer grade, 
        String email, 
        Long classId
    ) {}
}
