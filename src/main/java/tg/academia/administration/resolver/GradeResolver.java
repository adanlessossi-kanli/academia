package tg.academia.administration.resolver;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import tg.academia.administration.entity.Grade;
import tg.academia.administration.repository.GradeRepository;
import tg.academia.administration.repository.StudentRepository;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GradeResolver {
    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;

    @QueryMapping
    public List<Grade> grades() {
        return gradeRepository.findAll();
    }

    @QueryMapping
    public List<Grade> gradesByStudent(@Argument Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    @MutationMapping
    public Grade createGrade(@Argument Long studentId, @Argument String subject, 
                            @Argument Double score, @Argument String semester) {
        Grade grade = new Grade();
        grade.setStudent(studentRepository.findById(studentId).orElse(null));
        grade.setSubject(subject);
        grade.setScore(score);
        grade.setSemester(semester);
        return gradeRepository.save(grade);
    }
}
