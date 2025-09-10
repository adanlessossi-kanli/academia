package tg.academia.school.resolver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tg.academia.school.entity.Grade;
import tg.academia.school.entity.Student;
import tg.academia.school.repository.GradeRepository;
import tg.academia.school.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GradeResolverTest {

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private GradeResolver gradeResolver;

    @Test
    void grades_ReturnsAllGrades() {
        Grade grade = new Grade();
        grade.setSubject("Math");
        when(gradeRepository.findAll()).thenReturn(List.of(grade));

        List<Grade> result = gradeResolver.grades();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSubject()).isEqualTo("Math");
    }

    @Test
    void gradesByStudent_ReturnsGradesForStudent() {
        Grade grade = new Grade();
        grade.setSubject("Science");
        when(gradeRepository.findByStudentId(1L)).thenReturn(List.of(grade));

        List<Grade> result = gradeResolver.gradesByStudent(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSubject()).isEqualTo("Science");
    }

    @Test
    void createGrade_Success() {
        Student student = new Student();
        student.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(gradeRepository.save(any(Grade.class))).thenAnswer(i -> i.getArgument(0));

        Grade result = gradeResolver.createGrade(1L, "English", 88.5, "Spring 2024");

        assertThat(result.getStudent()).isEqualTo(student);
        assertThat(result.getSubject()).isEqualTo("English");
        assertThat(result.getScore()).isEqualTo(88.5);
    }
}