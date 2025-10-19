package tg.academia.administration.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import tg.academia.administration.exception.DuplicateResourceException;
import tg.academia.administration.service.StudentService;
import tg.academia.administration.service.TeacherService;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@org.springframework.context.annotation.Import(tg.academia.administration.config.TestSecurityConfig.class)
class ValidationIntegrationTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Test
    void createStudent_FailsWithDuplicateEmail() {
        studentService.createStudent("John", "Doe", 3, "duplicate@test.com", null);

        assertThatThrownBy(() -> studentService.createStudent("Jane", "Smith", 2, "duplicate@test.com", null))
            .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void createTeacher_FailsWithDuplicateEmail() {
        teacherService.createTeacher("John", "Doe", "teacher@test.com", "Math");

        assertThatThrownBy(() -> teacherService.createTeacher("Jane", "Smith", "teacher@test.com", "Science"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Email already exists");
    }
}
