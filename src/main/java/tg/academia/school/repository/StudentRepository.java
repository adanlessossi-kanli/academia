package tg.academia.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tg.academia.school.entity.Student;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByGrade(Integer grade);
    List<Student> findBySchoolClassId(Long classId);
    
    @Query("SELECT s FROM Student s WHERE LOWER(s.firstName) LIKE LOWER(CONCAT('%', ?1, '%')) OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<Student> searchByName(String name);
    
    boolean existsByEmail(String email);
}