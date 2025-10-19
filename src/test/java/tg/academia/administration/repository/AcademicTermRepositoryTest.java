package tg.academia.administration.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tg.academia.administration.entity.AcademicTerm;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AcademicTermRepositoryTest {

    @Autowired
    private AcademicTermRepository academicTermRepository;

    @Test
    void shouldSaveAndFindAcademicTerm() {
        AcademicTerm term = new AcademicTerm();
        term.setName("Fall 2023");
        term.setStartDate(LocalDate.of(2023, 9, 1));
        term.setEndDate(LocalDate.of(2023, 12, 15));
        term.setStatus(AcademicTerm.TermStatus.ACTIVE);
        term.setActive(true);

        AcademicTerm saved = academicTermRepository.save(term);
        assertNotNull(saved.getId());

        Optional<AcademicTerm> found = academicTermRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Fall 2023", found.get().getName());
    }

    @Test
    void shouldFindByActiveTrue() {
        AcademicTerm activeTermEntity = new AcademicTerm();
        activeTermEntity.setName("Active Term");
        activeTermEntity.setStartDate(LocalDate.of(2023, 9, 1));
        activeTermEntity.setEndDate(LocalDate.of(2023, 12, 15));
        activeTermEntity.setActive(true);
        academicTermRepository.save(activeTermEntity);

        AcademicTerm inactiveTerm = new AcademicTerm();
        inactiveTerm.setName("Inactive Term");
        inactiveTerm.setStartDate(LocalDate.of(2024, 1, 1));
        inactiveTerm.setEndDate(LocalDate.of(2024, 5, 15));
        inactiveTerm.setActive(false);
        academicTermRepository.save(inactiveTerm);

        Optional<AcademicTerm> foundTerm = academicTermRepository.findByActiveTrue();
        assertTrue(foundTerm.isPresent());
        assertEquals("Active Term", foundTerm.get().getName());
    }

    @Test
    void shouldFindByDate() {
        AcademicTerm term = new AcademicTerm();
        term.setName("Fall 2023");
        term.setStartDate(LocalDate.of(2023, 9, 1));
        term.setEndDate(LocalDate.of(2023, 12, 15));
        term.setActive(true);
        academicTermRepository.save(term);

        LocalDate testDate = LocalDate.of(2023, 10, 15);
        Optional<AcademicTerm> found = academicTermRepository.findByDate(testDate);
        assertTrue(found.isPresent());
        assertEquals("Fall 2023", found.get().getName());
    }
}