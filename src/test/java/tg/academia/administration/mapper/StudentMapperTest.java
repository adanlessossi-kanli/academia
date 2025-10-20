package tg.academia.administration.mapper;

import org.junit.jupiter.api.Test;
import tg.academia.administration.entity.SchoolClass;
import tg.academia.administration.entity.Student;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class StudentMapperTest {
    
    @Test
    void toResponse_WithSchoolClass_MapsCorrectly() {
        var schoolClass = SchoolClass.builder().id(1L).name("Class A").build();
        var student = Student.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .grade(3)
            .email("john@test.com")
            .schoolClass(schoolClass)
            .createdAt(LocalDateTime.now())
            .build();
        
        var response = StudentMapper.toResponse(student);
        
        assertEquals(1L, response.id());
        assertEquals("John Doe", response.fullName());
        assertEquals(1L, response.classId());
        assertEquals("Class A", response.className());
    }
    
    @Test
    void toResponse_WithoutSchoolClass_MapsCorrectly() {
        var student = Student.builder()
            .id(1L)
            .firstName("Jane")
            .lastName("Smith")
            .grade(4)
            .email("jane@test.com")
            .build();
        
        var response = StudentMapper.toResponse(student);
        
        assertNull(response.classId());
        assertNull(response.className());
    }
}
