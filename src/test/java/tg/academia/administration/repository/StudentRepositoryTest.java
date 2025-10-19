package tg.academia.administration.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import tg.academia.administration.config.AuditConfig;
import tg.academia.administration.entity.Student;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(AuditConfig.class)
class StudentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void findByGrade_ReturnsStudentsWithGrade() {
        Student student = new Student();
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setGrade(3);
        student.setEmail("john@test.com");
        entityManager.persistAndFlush(student);

        var result = studentRepository.findByGrade(3);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    void searchByName_FindsStudentsByPartialName() {
        Student student = new Student();
        student.setFirstName("Alice");
        student.setLastName("Johnson");
        student.setGrade(2);
        student.setEmail("alice@test.com");
        entityManager.persistAndFlush(student);

        var result = studentRepository.searchByName("Ali");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("Alice");
    }

    @Test
    void existsByEmail_ReturnsTrueWhenEmailExists() {
        Student student = new Student();
        student.setFirstName("Bob");
        student.setLastName("Smith");
        student.setGrade(1);
        student.setEmail("bob@test.com");
        entityManager.persistAndFlush(student);

        boolean exists = studentRepository.existsByEmail("bob@test.com");

        assertThat(exists).isTrue();
    }
}
