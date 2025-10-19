package tg.academia.administration.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import tg.academia.administration.config.AuditConfig;
import tg.academia.administration.entity.SchoolClass;
import tg.academia.administration.entity.Teacher;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(AuditConfig.class)
class SchoolClassRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SchoolClassRepository schoolClassRepository;

    @Test
    void findByGrade_ReturnsClassesWithGrade() {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName("Grade 3A");
        schoolClass.setGrade(3);
        schoolClass.setRoom("101");
        entityManager.persistAndFlush(schoolClass);

        var result = schoolClassRepository.findByGrade(3);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Grade 3A");
    }

    @Test
    void findByTeacherId_ReturnsClassesForTeacher() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setEmail("john@test.com");
        teacher.setSubject("Math");
        teacher = entityManager.persistAndFlush(teacher);

        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName("Math Class");
        schoolClass.setGrade(4);
        schoolClass.setRoom("201");
        schoolClass.setTeacher(teacher);
        entityManager.persistAndFlush(schoolClass);

        var result = schoolClassRepository.findByTeacherId(teacher.getId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Math Class");
    }
}
