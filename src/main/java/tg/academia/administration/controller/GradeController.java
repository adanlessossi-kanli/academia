package tg.academia.administration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tg.academia.administration.entity.Grade;
import tg.academia.administration.entity.Student;
import tg.academia.administration.repository.GradeRepository;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
@Tag(name = "Grades", description = "Grade management operations")
public class GradeController {
    
    private final GradeRepository gradeRepository;

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get grades by student")
    public List<Grade> getGradesByStudent(@PathVariable Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    @PostMapping
    @Operation(summary = "Create new grade")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MANAGER') or hasRole('TEACHER')")
    public Grade createGrade(@RequestBody CreateGradeRequest request) {
        Grade grade = new Grade();
        Student student = new Student();
        student.setId(request.studentId());
        grade.setStudent(student);
        grade.setSubject(request.subject());
        grade.setSemester(request.semester());
        grade.setScore(request.score());
        return gradeRepository.save(grade);
    }

    public record CreateGradeRequest(
        Long studentId,
        String subject,
        String semester,
        Double score
    ) {}
}