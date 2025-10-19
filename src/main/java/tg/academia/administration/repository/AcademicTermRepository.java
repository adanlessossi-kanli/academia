package tg.academia.administration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tg.academia.administration.entity.AcademicTerm;

import java.time.LocalDate;
import java.util.Optional;

public interface AcademicTermRepository extends JpaRepository<AcademicTerm, Long> {
    
    Optional<AcademicTerm> findByActiveTrue();
    
    @Query("SELECT t FROM AcademicTerm t WHERE :date BETWEEN t.startDate AND t.endDate")
    Optional<AcademicTerm> findByDate(LocalDate date);
}