package tg.academia.administration.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {

    @Test
    void shouldCreateTeacherWithValidData() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Jane");
        teacher.setLastName("Smith");
        teacher.setEmail("jane.smith@school.edu");
        teacher.setSubject("Mathematics");

        assertEquals("Jane", teacher.getFirstName());
        assertEquals("Smith", teacher.getLastName());
        assertEquals("jane.smith@school.edu", teacher.getEmail());
        assertEquals("Mathematics", teacher.getSubject());
    }
}