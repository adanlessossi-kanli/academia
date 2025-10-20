package tg.academia.administration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tg.academia.administration.entity.Student;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByGradeOrderByLastNameAscFirstNameAsc(Integer grade);
    List<Student> findBySchoolClassIdOrderByLastNameAscFirstNameAsc(Long classId);
    
    @Deprecated(forRemoval = true)
    default List<Student> findByGrade(Integer grade) {
        return findByGradeOrderByLastNameAscFirstNameAsc(grade);
    }
    
    @Deprecated(forRemoval = true)
    default List<Student> findBySchoolClassId(Long classId) {
        return findBySchoolClassIdOrderByLastNameAscFirstNameAsc(classId);
    }
    
    @Query("""
            SELECT s FROM Student s 
            WHERE LOWER(s.firstName) LIKE LOWER(CONCAT('%', :name, '%')) 
            OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :name, '%'))
            ORDER BY s.lastName, s.firstName
            """)
    List<Student> searchByName(@Param("name") String name);
    
    boolean existsByEmail(String email);
}
