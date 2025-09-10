package tg.academia.school.resolver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tg.academia.school.entity.Teacher;
import tg.academia.school.repository.TeacherRepository;
import tg.academia.school.service.TeacherService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherResolverTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private TeacherResolver teacherResolver;

    @Test
    void teachers_ReturnsAllTeachers() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        when(teacherRepository.findAll()).thenReturn(List.of(teacher));

        List<Teacher> result = teacherResolver.teachers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    void searchTeachers_ReturnsMatchingTeachers() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Jane");
        when(teacherRepository.searchByName("Jan")).thenReturn(List.of(teacher));

        List<Teacher> result = teacherResolver.searchTeachers("Jan");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("Jane");
    }

    @Test
    void createTeacher_Success() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Bob");
        when(teacherService.createTeacher("Bob", "Smith", "bob@test.com", "Math"))
            .thenReturn(teacher);

        Teacher result = teacherResolver.createTeacher("Bob", "Smith", "bob@test.com", "Math");

        assertThat(result.getFirstName()).isEqualTo("Bob");
    }
}