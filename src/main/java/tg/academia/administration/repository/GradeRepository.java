package tg.academia.administration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.academia.administration.entity.Grade;
import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudentId(Long studentId);
    List<Grade> findBySubject(String subject);
}
