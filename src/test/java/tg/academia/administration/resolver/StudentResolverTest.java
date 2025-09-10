package tg.academia.administration.resolver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import tg.academia.administration.entity.Student;
import tg.academia.administration.repository.SchoolClassRepository;
import tg.academia.administration.repository.StudentRepository;
import tg.academia.administration.service.StudentService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentResolverTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SchoolClassRepository schoolClassRepository;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentResolver studentResolver;

    @Test
    void students_ReturnsAllStudents() {
        Student student = new Student();
        student.setFirstName("John");
        when(studentRepository.findAll()).thenReturn(List.of(student));

        List<Student> result = studentResolver.students(null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    void studentsByGrade_ReturnsStudentsForGrade() {
        Student student = new Student();
        student.setGrade(3);
        when(studentRepository.findByGrade(3)).thenReturn(List.of(student));

        List<Student> result = studentResolver.studentsByGrade(3);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getGrade()).isEqualTo(3);
    }

    @Test
    void createStudent_Success() {
        Student student = new Student();
        student.setFirstName("Jane");
        when(studentService.createStudent("Jane", "Doe", 2, "jane@test.com", null))
            .thenReturn(student);

        Student result = studentResolver.createStudent("Jane", "Doe", 2, "jane@test.com", null);

        assertThat(result.getFirstName()).isEqualTo("Jane");
    }

    @Test
    void createStudent_ThrowsRuntimeExceptionOnServiceError() {
        when(studentService.createStudent("Jane", "Doe", 2, "jane@test.com", null))
            .thenThrow(new IllegalArgumentException("Email exists"));

        assertThatThrownBy(() -> studentResolver.createStudent("Jane", "Doe", 2, "jane@test.com", null))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Email exists");
    }

    @Test
    void searchStudents_ReturnsMatchingStudents() {
        Student student = new Student();
        student.setFirstName("Alice");
        when(studentRepository.searchByName("Ali")).thenReturn(List.of(student));

        List<Student> result = studentResolver.searchStudents("Ali");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("Alice");
    }

    @Test
    void students_WithLimit_ReturnsLimitedResults() {
        Student student = new Student();
        when(studentRepository.findAll(any(org.springframework.data.domain.Pageable.class)))
            .thenReturn(new PageImpl<>(List.of(student)));

        List<Student> result = studentResolver.students(1);

        assertThat(result).hasSize(1);
    }
}
