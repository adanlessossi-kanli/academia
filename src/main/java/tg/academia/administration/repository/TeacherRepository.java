package tg.academia.administration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tg.academia.administration.entity.Teacher;
import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    @Query("""
            SELECT t FROM Teacher t 
            WHERE LOWER(t.firstName) LIKE LOWER(CONCAT('%', :name, '%')) 
            OR LOWER(t.lastName) LIKE LOWER(CONCAT('%', :name, '%'))
            """)
    List<Teacher> searchByName(@Param("name") String name);
    
    boolean existsByEmail(String email);
}
