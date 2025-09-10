package tg.academia.school.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tg.academia.school.entity.SchoolClass;
import tg.academia.school.entity.Student;
import tg.academia.school.repository.SchoolClassRepository;
import tg.academia.school.repository.StudentRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SchoolClassRepository schoolClassRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void createStudent_Success() {
        when(studentRepository.existsByEmail("test@email.com")).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenAnswer(i -> i.getArgument(0));

        Student result = studentService.createStudent("John", "Doe", 3, "test@email.com", null);

        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getGrade()).isEqualTo(3);
    }

    @Test
    void createStudent_ThrowsExceptionWhenEmailExists() {
        when(studentRepository.existsByEmail("test@email.com")).thenReturn(true);

        assertThatThrownBy(() -> studentService.createStudent("John", "Doe", 3, "test@email.com", null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Email already exists");
    }

    @Test
    void createStudent_ThrowsExceptionWhenGradeMismatch() {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setGrade(2);
        
        when(studentRepository.existsByEmail("test@email.com")).thenReturn(false);
        when(schoolClassRepository.findById(1L)).thenReturn(Optional.of(schoolClass));

        assertThatThrownBy(() -> studentService.createStudent("John", "Doe", 3, "test@email.com", 1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Student grade must match class grade");
    }
}