package tg.academia.administration.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tg.academia.administration.dto.TeacherRequest;
import tg.academia.administration.entity.Teacher;
import tg.academia.administration.repository.TeacherRepository;
import tg.academia.administration.service.TeacherService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private TeacherController teacherController;

    @Test
    void getAllTeachers_ReturnsAllTeachers() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        when(teacherRepository.findAll()).thenReturn(List.of(teacher));

        List<Teacher> result = teacherController.getAllTeachers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    void searchTeachers_ReturnsMatchingTeachers() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Jane");
        when(teacherRepository.searchByName("Jan")).thenReturn(List.of(teacher));

        List<Teacher> result = teacherController.searchTeachers("Jan");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("Jane");
    }

    @Test
    void createTeacher_ReturnsCreatedTeacher() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Bob");
        var request = new TeacherRequest("Bob", "Smith", "bob@test.com", "Math");
        when(teacherService.createTeacher("Bob", "Smith", "bob@test.com", "Math")).thenReturn(teacher);

        Teacher result = teacherController.createTeacher(request);

        assertThat(result.getFirstName()).isEqualTo("Bob");
    }
}
