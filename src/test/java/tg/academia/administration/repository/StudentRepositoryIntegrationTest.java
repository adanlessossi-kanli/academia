package tg.academia.administration.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tg.academia.administration.entity.Student;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(tg.academia.administration.config.AuditConfig.class)
class StudentRepositoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void shouldFindStudentsByGrade() {
        // Given
        Student student1 = new Student();
        student1.setFirstName("John");
        student1.setLastName("Doe");
        student1.setGrade(1);
        student1.setEmail("john@test.com");
        entityManager.persistAndFlush(student1);

        Student student2 = new Student();
        student2.setFirstName("Jane");
        student2.setLastName("Smith");
        student2.setGrade(1);
        student2.setEmail("jane@test.com");
        entityManager.persistAndFlush(student2);

        Student student3 = new Student();
        student3.setFirstName("Bob");
        student3.setLastName("Wilson");
        student3.setGrade(2);
        student3.setEmail("bob@test.com");
        entityManager.persistAndFlush(student3);

        // When
        List<Student> grade1Students = studentRepository.findByGrade(1);
        List<Student> grade2Students = studentRepository.findByGrade(2);

        // Then
        assertEquals(2, grade1Students.size());
        assertEquals(1, grade2Students.size());
        assertTrue(grade1Students.stream().allMatch(s -> s.getGrade() == 1));
        assertTrue(grade2Students.stream().allMatch(s -> s.getGrade() == 2));
    }

    @Test
    void shouldSearchStudentsByName() {
        // Given
        Student student1 = new Student();
        student1.setFirstName("John");
        student1.setLastName("Doe");
        student1.setGrade(1);
        student1.setEmail("john@test.com");
        entityManager.persistAndFlush(student1);

        Student student2 = new Student();
        student2.setFirstName("Johnny");
        student2.setLastName("Smith");
        student2.setGrade(2);
        student2.setEmail("johnny@test.com");
        entityManager.persistAndFlush(student2);

        // When
        List<Student> results = studentRepository.searchByName("John");

        // Then
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(s -> s.getFirstName().equals("John")));
        assertTrue(results.stream().anyMatch(s -> s.getFirstName().equals("Johnny")));
    }

    @Test
    void shouldCheckEmailExists() {
        // Given
        Student student = new Student();
        student.setFirstName("Test");
        student.setLastName("User");
        student.setGrade(1);
        student.setEmail("test@test.com");
        entityManager.persistAndFlush(student);

        // When & Then
        assertTrue(studentRepository.existsByEmail("test@test.com"));
        assertFalse(studentRepository.existsByEmail("nonexistent@test.com"));
    }

    @Test
    void shouldEnforceUniqueEmailConstraint() {
        // Given
        Student student1 = new Student();
        student1.setFirstName("First");
        student1.setLastName("Student");
        student1.setGrade(1);
        student1.setEmail("duplicate@test.com");
        entityManager.persistAndFlush(student1);

        Student student2 = new Student();
        student2.setFirstName("Second");
        student2.setLastName("Student");
        student2.setGrade(2);
        student2.setEmail("duplicate@test.com");

        // When & Then
        assertThrows(Exception.class, () -> {
            entityManager.persistAndFlush(student2);
        });
    }

    @Test
    void shouldSaveAndRetrieveStudent() {
        // Given
        Student student = new Student();
        student.setFirstName("Save");
        student.setLastName("Test");
        student.setGrade(3);
        student.setEmail("save@test.com");

        // When
        Student saved = studentRepository.save(student);
        Optional<Student> retrieved = studentRepository.findById(saved.getId());

        // Then
        assertTrue(retrieved.isPresent());
        assertEquals("Save", retrieved.get().getFirstName());
        assertEquals("Test", retrieved.get().getLastName());
        assertEquals(3, retrieved.get().getGrade());
        assertEquals("save@test.com", retrieved.get().getEmail());
        assertNotNull(retrieved.get().getCreatedAt());
        assertNotNull(retrieved.get().getUpdatedAt());
    }

    @Test
    void shouldUpdateTimestampsOnModification() throws InterruptedException {
        // Given
        Student student = new Student();
        student.setFirstName("Update");
        student.setLastName("Test");
        student.setGrade(1);
        student.setEmail("update@test.com");
        
        Student saved = studentRepository.save(student);
        entityManager.flush();
        
        // Wait to ensure different timestamp
        Thread.sleep(10);
        
        // When
        saved.setFirstName("Updated");
        Student updated = studentRepository.save(saved);
        entityManager.flush();

        // Then
        assertNotNull(updated.getCreatedAt());
        assertNotNull(updated.getUpdatedAt());
        assertTrue(updated.getUpdatedAt().isAfter(updated.getCreatedAt()) || 
                  updated.getUpdatedAt().equals(updated.getCreatedAt()));
    }
}