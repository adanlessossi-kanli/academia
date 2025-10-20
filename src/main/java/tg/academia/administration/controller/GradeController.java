package tg.academia.administration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tg.academia.administration.dto.GradeRequest;
import tg.academia.administration.entity.Grade;
import tg.academia.administration.exception.ResourceNotFoundException;
import tg.academia.administration.repository.GradeRepository;
import tg.academia.administration.repository.StudentRepository;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
@Tag(name = "Grades", description = "Grade management operations")
public class GradeController {
    
    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get grades by student")
    public List<Grade> getGradesByStudent(@PathVariable Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new grade")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Grade createGrade(@Valid @RequestBody GradeRequest request) {
        var student = studentRepository.findById(request.studentId())
            .orElseThrow(() -> new ResourceNotFoundException("Student", request.studentId()));
        
        var grade = new Grade();
        grade.setStudent(student);
        grade.setSubject(request.subject());
        grade.setSemester(request.semester());
        grade.setScore(request.score());
        
        return gradeRepository.save(grade);
    }
}