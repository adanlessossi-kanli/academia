package tg.academia.administration.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import tg.academia.administration.entity.SchoolClass;
import tg.academia.administration.entity.Student;
import tg.academia.administration.exception.DuplicateResourceException;
import tg.academia.administration.exception.ResourceNotFoundException;
import tg.academia.administration.repository.SchoolClassRepository;
import tg.academia.administration.repository.StudentRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SchoolClassRepository schoolClassRepository;

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentRepository, schoolClassRepository);
    }

    @Test
    void shouldCreateStudent() {
        // Given
        String firstName = "John";
        String lastName = "Doe";
        Integer grade = 1;
        String email = "john.doe@test.com";
        Long classId = 1L;

        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setId(classId);
        schoolClass.setGrade(grade);

        Student savedStudent = new Student();
        savedStudent.setId(1L);
        savedStudent.setFirstName(firstName);
        savedStudent.setLastName(lastName);
        savedStudent.setGrade(grade);
        savedStudent.setEmail(email);
        savedStudent.setSchoolClass(schoolClass);

        when(studentRepository.existsByEmail(email)).thenReturn(false);
        when(schoolClassRepository.findById(classId)).thenReturn(Optional.of(schoolClass));
        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        // When
        Student result = studentService.createStudent(firstName, lastName, grade, email, classId);

        // Then
        assertNotNull(result);
        assertEquals(firstName, result.getFirstName());
        assertEquals(lastName, result.getLastName());
        assertEquals(grade, result.getGrade());
        assertEquals(email, result.getEmail());
        assertEquals(schoolClass, result.getSchoolClass());

        verify(studentRepository).existsByEmail(email);
        verify(schoolClassRepository).findById(classId);
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailExists() {
        // Given
        String email = "existing@test.com";
        when(studentRepository.existsByEmail(email)).thenReturn(true);

        // When & Then
        assertThrows(DuplicateResourceException.class, () ->
                studentService.createStudent("John", "Doe", 1, email, null));

        verify(studentRepository).existsByEmail(email);
        verify(studentRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenClassNotFound() {
        // Given
        Long classId = 999L;
        when(studentRepository.existsByEmail(any())).thenReturn(false);
        when(schoolClassRepository.findById(classId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () ->
                studentService.createStudent("John", "Doe", 1, "test@test.com", classId));

        verify(schoolClassRepository).findById(classId);
        verify(studentRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenGradeMismatch() {
        // Given
        Long classId = 1L;
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setId(classId);
        schoolClass.setGrade(2); // Different grade

        when(studentRepository.existsByEmail(any())).thenReturn(false);
        when(schoolClassRepository.findById(classId)).thenReturn(Optional.of(schoolClass));

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                studentService.createStudent("John", "Doe", 1, "test@test.com", classId));
    }

    @Test
    void shouldUpdateStudent() {
        // Given
        Long studentId = 1L;
        Student existingStudent = new Student();
        existingStudent.setId(studentId);
        existingStudent.setEmail("old@test.com");

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.existsByEmail("new@test.com")).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(existingStudent);

        // When
        Student result = studentService.updateStudent(studentId, "Jane", "Smith", 2, "new@test.com", null);

        // Then
        assertNotNull(result);
        verify(studentRepository).findById(studentId);
        verify(studentRepository).save(existingStudent);
    }

    @Test
    void shouldGetStudentsByGrade() {
        // Given
        Integer grade = 1;
        List<Student> students = Arrays.asList(new Student(), new Student());
        when(studentRepository.findByGrade(grade)).thenReturn(students);

        // When
        List<Student> result = studentService.getStudentsByGrade(grade);

        // Then
        assertEquals(2, result.size());
        verify(studentRepository).findByGrade(grade);
    }

    @Test
    void shouldHandleDataIntegrityViolation() {
        // Given
        when(studentRepository.existsByEmail(any())).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenThrow(new DataIntegrityViolationException("Duplicate"));

        // When & Then
        assertThrows(DataIntegrityViolationException.class, () ->
                studentService.createStudent("John", "Doe", 1, "test@test.com", null));
    }
}