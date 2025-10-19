package tg.academia.administration.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GradeTest {

    @Test
    void shouldCreateGradeWithValidData() {
        Grade grade = new Grade();
        grade.setSubject("Mathematics");
        grade.setScore(85.5);
        grade.setSemester("Fall 2023");

        assertEquals("Mathematics", grade.getSubject());
        assertEquals(85.5, grade.getScore());
        assertEquals("Fall 2023", grade.getSemester());
    }

    @Test
    void shouldAllowValidScoreRange() {
        Grade grade = new Grade();
        
        grade.setScore(0.0);
        assertEquals(0.0, grade.getScore());
        
        grade.setScore(100.0);
        assertEquals(100.0, grade.getScore());
    }
}