package tg.academia.administration.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tg.academia.administration.entity.Teacher;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TeacherRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    void searchByName_FindsTeachersByPartialName() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Jane");
        teacher.setLastName("Smith");
        teacher.setEmail("jane@test.com");
        teacher.setSubject("Math");
        entityManager.persistAndFlush(teacher);

        var result = teacherRepository.searchByName("Jan");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("Jane");
    }

    @Test
    void existsByEmail_ReturnsTrueWhenEmailExists() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setEmail("john@test.com");
        teacher.setSubject("Science");
        entityManager.persistAndFlush(teacher);

        boolean exists = teacherRepository.existsByEmail("john@test.com");

        assertThat(exists).isTrue();
    }
}
