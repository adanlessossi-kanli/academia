package tg.academia.school.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class EntityValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void student_ValidEntity_NoViolations() {
        Student student = new Student();
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setGrade(3);
        student.setEmail("john@test.com");

        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertThat(violations).isEmpty();
    }

    @Test
    void student_InvalidGrade_HasViolations() {
        Student student = new Student();
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setGrade(7);
        student.setEmail("john@test.com");

        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("6");
    }

    @Test
    void student_InvalidEmail_HasViolations() {
        Student student = new Student();
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setGrade(3);
        student.setEmail("invalid-email");

        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertThat(violations).hasSize(1);
    }

    @Test
    void grade_InvalidScore_HasViolations() {
        Grade grade = new Grade();
        grade.setSubject("Math");
        grade.setScore(150.0);
        grade.setSemester("Fall 2024");

        Set<ConstraintViolation<Grade>> violations = validator.validate(grade);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("100");
    }

    @Test
    void teacher_BlankFields_HasViolations() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("");
        teacher.setLastName("");
        teacher.setEmail("teacher@test.com");
        teacher.setSubject("");

        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);

        assertThat(violations).hasSize(3);
    }
}