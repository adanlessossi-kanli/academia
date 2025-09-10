package tg.academia.school.resolver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tg.academia.school.entity.SchoolClass;
import tg.academia.school.entity.Teacher;
import tg.academia.school.repository.SchoolClassRepository;
import tg.academia.school.repository.TeacherRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchoolClassResolverTest {

    @Mock
    private SchoolClassRepository schoolClassRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private SchoolClassResolver schoolClassResolver;

    @Test
    void classes_ReturnsAllClasses() {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName("Grade 3A");
        when(schoolClassRepository.findAll()).thenReturn(List.of(schoolClass));

        List<SchoolClass> result = schoolClassResolver.classes();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Grade 3A");
    }

    @Test
    void classesByGrade_ReturnsClassesForGrade() {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setGrade(4);
        when(schoolClassRepository.findByGrade(4)).thenReturn(List.of(schoolClass));

        List<SchoolClass> result = schoolClassResolver.classesByGrade(4);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getGrade()).isEqualTo(4);
    }

    @Test
    void createClass_Success() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(schoolClassRepository.save(any(SchoolClass.class))).thenAnswer(i -> i.getArgument(0));

        SchoolClass result = schoolClassResolver.createClass("Grade 5A", 5, "501", 1L);

        assertThat(result.getName()).isEqualTo("Grade 5A");
        assertThat(result.getGrade()).isEqualTo(5);
        assertThat(result.getTeacher()).isEqualTo(teacher);
    }

    @Test
    void createClass_WithoutTeacher() {
        when(schoolClassRepository.save(any(SchoolClass.class))).thenAnswer(i -> i.getArgument(0));

        SchoolClass result = schoolClassResolver.createClass("Grade 6A", 6, "601", null);

        assertThat(result.getName()).isEqualTo("Grade 6A");
        assertThat(result.getTeacher()).isNull();
    }
}