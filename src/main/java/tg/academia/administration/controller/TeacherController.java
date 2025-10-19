package tg.academia.administration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tg.academia.administration.entity.Teacher;
import tg.academia.administration.repository.TeacherRepository;
import tg.academia.administration.service.TeacherService;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
@Tag(name = "Teachers", description = "Teacher management operations")
public class TeacherController {
    
    private final TeacherRepository teacherRepository;
    private final TeacherService teacherService;

    @GetMapping
    @Operation(summary = "Get all teachers")
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @GetMapping("/search")
    @Operation(summary = "Search teachers by name")
    public List<Teacher> searchTeachers(@RequestParam String name) {
        return teacherRepository.searchByName(name);
    }

    @PostMapping
    @Operation(summary = "Create new teacher")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MANAGER')")
    public Teacher createTeacher(@RequestBody CreateTeacherRequest request) {
        return teacherService.createTeacher(
            request.firstName(), 
            request.lastName(), 
            request.email(), 
            request.subject()
        );
    }

    public record CreateTeacherRequest(
        String firstName, 
        String lastName, 
        String email, 
        String subject
    ) {}
}
