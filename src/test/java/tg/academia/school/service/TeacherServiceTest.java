package tg.academia.school.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tg.academia.school.entity.Teacher;
import tg.academia.school.repository.TeacherRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    void createTeacher_Success() {
        when(teacherRepository.existsByEmail("teacher@email.com")).thenReturn(false);
        when(teacherRepository.save(any(Teacher.class))).thenAnswer(i -> i.getArgument(0));

        Teacher result = teacherService.createTeacher("Jane", "Smith", "teacher@email.com", "Math");

        assertThat(result.getFirstName()).isEqualTo("Jane");
        assertThat(result.getSubject()).isEqualTo("Math");
    }

    @Test
    void createTeacher_ThrowsExceptionWhenEmailExists() {
        when(teacherRepository.existsByEmail("teacher@email.com")).thenReturn(true);

        assertThatThrownBy(() -> teacherService.createTeacher("Jane", "Smith", "teacher@email.com", "Math"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Email already exists");
    }
}