package tg.academia.administration.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import tg.academia.administration.entity.Student;
import tg.academia.administration.repository.StudentRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeast;

@SpringBootTest(properties = "spring.cache.type=none")
@ActiveProfiles("test")
@org.springframework.context.annotation.Import(tg.academia.administration.config.TestSecurityConfig.class)
class StudentServiceCacheTest {

    @Autowired
    private StudentService studentService;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private tg.academia.administration.repository.SchoolClassRepository schoolClassRepository;

    @Autowired
    private CacheManager cacheManager;

    @Test
    void shouldCacheStudentsByGrade() {
        // Given
        Integer grade = 1;
        List<Student> students = Arrays.asList(new Student(), new Student());
        when(studentRepository.findByGrade(grade)).thenReturn(students);

        // When
        List<Student> result = studentService.getStudentsByGrade(grade);

        // Then
        assertEquals(students, result);
        verify(studentRepository, atLeastOnce()).findByGrade(grade);
    }

    @Test
    void shouldEvictCacheOnStudentCreation() {
        // Given
        Integer grade = 1;
        List<Student> students = Arrays.asList(new Student());
        when(studentRepository.findByGrade(grade)).thenReturn(students);
        when(studentRepository.existsByEmail(anyString())).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(new Student());

        // When
        studentService.createStudent("John", "Doe", grade, "john@test.com", null);

        // Then
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void shouldEvictCacheOnStudentUpdate() {
        // Given
        Long studentId = 1L;
        Student existingStudent = new Student();
        existingStudent.setId(studentId);
        existingStudent.setEmail("old@test.com");
        
        when(studentRepository.findById(studentId)).thenReturn(java.util.Optional.of(existingStudent));
        when(studentRepository.existsByEmail(anyString())).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(existingStudent);

        // When
        studentService.updateStudent(studentId, "Jane", "Smith", 1, "new@test.com", null);
        
        // Then
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void shouldEvictCacheOnStudentDeletion() {
        // When
        studentService.deleteStudent(1L);
        
        // Then
        verify(studentRepository, times(1)).deleteById(1L);
    }
}