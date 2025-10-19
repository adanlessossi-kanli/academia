package tg.academia.administration.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SchoolClassTest {

    @Test
    void shouldCreateSchoolClassWithValidData() {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName("3A");
        schoolClass.setGrade(3);
        schoolClass.setRoom("Room 101");

        assertEquals("3A", schoolClass.getName());
        assertEquals(3, schoolClass.getGrade());
        assertEquals("Room 101", schoolClass.getRoom());
    }

    @Test
    void shouldAllowValidGradeRange() {
        SchoolClass schoolClass = new SchoolClass();
        
        schoolClass.setGrade(1);
        assertEquals(1, schoolClass.getGrade());
        
        schoolClass.setGrade(6);
        assertEquals(6, schoolClass.getGrade());
    }
}