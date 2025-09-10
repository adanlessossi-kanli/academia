package tg.academia.administration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.academia.administration.entity.SchoolClass;
import java.util.List;

public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {
    List<SchoolClass> findByGrade(Integer grade);
    List<SchoolClass> findByTeacherId(Long teacherId);
}
