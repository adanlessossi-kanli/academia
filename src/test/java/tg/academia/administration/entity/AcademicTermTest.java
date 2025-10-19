package tg.academia.administration.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class AcademicTermTest {

    @Test
    void shouldCreateAcademicTermWithValidData() {
        AcademicTerm term = new AcademicTerm();
        LocalDate startDate = LocalDate.of(2023, 9, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 15);
        
        term.setName("Fall 2023");
        term.setStartDate(startDate);
        term.setEndDate(endDate);
        term.setStatus(AcademicTerm.TermStatus.ACTIVE);
        term.setActive(true);

        assertEquals("Fall 2023", term.getName());
        assertEquals(startDate, term.getStartDate());
        assertEquals(endDate, term.getEndDate());
        assertEquals(AcademicTerm.TermStatus.ACTIVE, term.getStatus());
        assertTrue(term.isActive());
    }

    @Test
    void shouldHaveDefaultValues() {
        AcademicTerm term = new AcademicTerm();
        
        assertEquals(AcademicTerm.TermStatus.PLANNED, term.getStatus());
        assertFalse(term.isActive());
    }

    @Test
    void shouldHaveAllTermStatuses() {
        assertEquals(3, AcademicTerm.TermStatus.values().length);
        assertTrue(java.util.Arrays.asList(AcademicTerm.TermStatus.values())
                .contains(AcademicTerm.TermStatus.PLANNED));
        assertTrue(java.util.Arrays.asList(AcademicTerm.TermStatus.values())
                .contains(AcademicTerm.TermStatus.ACTIVE));
        assertTrue(java.util.Arrays.asList(AcademicTerm.TermStatus.values())
                .contains(AcademicTerm.TermStatus.COMPLETED));
    }
}