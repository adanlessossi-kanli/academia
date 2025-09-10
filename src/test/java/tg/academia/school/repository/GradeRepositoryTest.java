package tg.academia.school.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tg.academia.school.entity.Grade;
import tg.academia.school.entity.Student;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GradeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GradeRepository gradeRepository;

    @Test
    void findByStudentId_ReturnsGradesForStudent() {
        Student student = new Student();
        student.setFirstName("Alice");
        student.setLastName("Johnson");
        student.setGrade(2);
        student.setEmail("alice@test.com");
        student = entityManager.persistAndFlush(student);

        Grade grade = new Grade();
        grade.setStudent(student);
        grade.setSubject("Math");
        grade.setScore(85.5);
        grade.setSemester("Fall 2024");
        entityManager.persistAndFlush(grade);

        var result = gradeRepository.findByStudentId(student.getId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSubject()).isEqualTo("Math");
    }

    @Test
    void findBySubject_ReturnsGradesForSubject() {
        Student student = new Student();
        student.setFirstName("Bob");
        student.setLastName("Smith");
        student.setGrade(3);
        student.setEmail("bob@test.com");
        student = entityManager.persistAndFlush(student);

        Grade grade = new Grade();
        grade.setStudent(student);
        grade.setSubject("Science");
        grade.setScore(92.0);
        grade.setSemester("Spring 2024");
        entityManager.persistAndFlush(grade);

        var result = gradeRepository.findBySubject("Science");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getScore()).isEqualTo(92.0);
    }
}