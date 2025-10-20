package tg.academia.administration.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tg.academia.administration.entity.Student;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.schoolClass WHERE s.grade = :grade ORDER BY s.lastName, s.firstName")
    List<Student> findByGradeOrderByLastNameAscFirstNameAsc(@Param("grade") Integer grade);
    
    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.schoolClass WHERE s.schoolClass.id = :classId ORDER BY s.lastName, s.firstName")
    List<Student> findBySchoolClassIdOrderByLastNameAscFirstNameAsc(@Param("classId") Long classId);
    
    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.schoolClass WHERE s.id IN :ids")
    List<Student> findByIdsWithClass(@Param("ids") List<Long> ids);
    
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
    
    @Query("""
            SELECT s FROM Student s 
            WHERE LOWER(s.firstName) LIKE LOWER(CONCAT('%', :name, '%')) 
            OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :name, '%'))
            ORDER BY s.lastName, s.firstName
            """)
    Page<Student> searchByNamePageable(@Param("name") String name, Pageable pageable);
    
    boolean existsByEmail(String email);
}
