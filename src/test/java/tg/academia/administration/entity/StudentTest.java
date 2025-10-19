package tg.academia.administration.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void shouldCreateStudentWithValidData() {
        Student student = new Student();
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setGrade(3);
        student.setEmail("john.doe@school.edu");

        assertEquals("John", student.getFirstName());
        assertEquals("Doe", student.getLastName());
        assertEquals(3, student.getGrade());
        assertEquals("john.doe@school.edu", student.getEmail());
    }

    @Test
    void shouldAllowValidGradeRange() {
        Student student = new Student();
        
        student.setGrade(1);
        assertEquals(1, student.getGrade());
        
        student.setGrade(6);
        assertEquals(6, student.getGrade());
    }
}