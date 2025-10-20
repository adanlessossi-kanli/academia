package tg.academia.administration.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import tg.academia.administration.controller.BulkOperationsController.BulkAttendanceRequest;
import tg.academia.administration.entity.Attendance;
import tg.academia.administration.entity.Student;
import tg.academia.administration.repository.AttendanceRepository;
import tg.academia.administration.repository.StudentRepository;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BulkOperationsServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private AttendanceRepository attendanceRepository;

    private BulkOperationsService bulkOperationsService;

    @BeforeEach
    void setUp() {
        bulkOperationsService = new BulkOperationsService(studentRepository, attendanceRepository);
    }

    @Test
    void shouldImportStudentsFromCsv() {
        // Given
        String csvContent = "First Name,Last Name,Grade,Email\n" +
                "John,Doe,1,john@test.com\n" +
                "Jane,Smith,2,jane@test.com";
        
        MockMultipartFile file = new MockMultipartFile(
                "file", "students.csv", "text/csv", csvContent.getBytes(StandardCharsets.UTF_8));

        when(studentRepository.saveAll(any(List.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Map<String, Object> result = bulkOperationsService.importStudents(file);

        // Then
        assertEquals(2, result.get("successCount"));
        assertEquals(0, result.get("errorCount"));
        assertTrue(((List<?>) result.get("errors")).isEmpty());
        
        verify(studentRepository, times(1)).saveAll(any(List.class));
    }

    @Test
    void shouldHandleImportErrors() {
        // Given
        String csvContent = "First Name,Last Name,Grade,Email\n" +
                "John,Doe,invalid,john@test.com\n" +
                "Jane,Smith,2,jane@test.com";
        
        MockMultipartFile file = new MockMultipartFile(
                "file", "students.csv", "text/csv", csvContent.getBytes(StandardCharsets.UTF_8));

        when(studentRepository.saveAll(any(List.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Map<String, Object> result = bulkOperationsService.importStudents(file);

        // Then
        assertEquals(1, result.get("successCount"));
        assertEquals(1, result.get("errorCount"));
        assertFalse(((List<?>) result.get("errors")).isEmpty());
    }

    @Test
    void shouldExportStudentsToCsv() {
        // Given
        Student student1 = new Student();
        student1.setFirstName("John");
        student1.setLastName("Doe");
        student1.setGrade(1);
        student1.setEmail("john@test.com");

        Student student2 = new Student();
        student2.setFirstName("Jane");
        student2.setLastName("Smith");
        student2.setGrade(2);
        student2.setEmail("jane@test.com");

        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1, student2));

        // When
        byte[] result = bulkOperationsService.exportStudents();

        // Then
        String csvContent = new String(result, StandardCharsets.UTF_8);
        assertTrue(csvContent.contains("First Name,Last Name,Grade,Email"));
        assertTrue(csvContent.contains("John,Doe,1,john@test.com"));
        assertTrue(csvContent.contains("Jane,Smith,2,jane@test.com"));
    }

    @Test
    void shouldMarkBulkAttendance() {
        // Given
        Student student1 = new Student();
        student1.setId(1L);
        Student student2 = new Student();
        student2.setId(2L);

        when(studentRepository.findAllById(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(student1, student2));
        when(attendanceRepository.saveAll(any(List.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BulkAttendanceRequest request = new BulkAttendanceRequest(
                Arrays.asList(1L, 2L), "2024-01-15", "PRESENT");

        // When
        Map<String, Object> result = bulkOperationsService.markBulkAttendance(request);

        // Then
        assertEquals(2, result.get("successCount"));
        assertEquals(0, result.get("errorCount"));
        assertTrue(((List<?>) result.get("errors")).isEmpty());
        
        verify(attendanceRepository, times(1)).saveAll(any(List.class));
    }

    @Test
    void shouldHandleBulkAttendanceErrors() {
        // Given
        Student student2 = new Student();
        student2.setId(2L);
        when(studentRepository.findAllById(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(student2));
        when(attendanceRepository.saveAll(any(List.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BulkAttendanceRequest request = new BulkAttendanceRequest(
                Arrays.asList(1L, 2L), "2024-01-15", "PRESENT");

        // When
        Map<String, Object> result = bulkOperationsService.markBulkAttendance(request);

        // Then
        assertEquals(1, result.get("successCount"));
        assertEquals(1, result.get("errorCount"));
        assertFalse(((List<?>) result.get("errors")).isEmpty());
    }

    @Test
    void shouldHandleEmptyFile() {
        // Given
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "empty.csv", "text/csv", "".getBytes(StandardCharsets.UTF_8));

        // When
        Map<String, Object> result = bulkOperationsService.importStudents(emptyFile);

        // Then
        assertEquals(0, result.get("successCount"));
        assertEquals(0, result.get("errorCount"));
        
        verify(studentRepository, never()).save(any());
    }
}