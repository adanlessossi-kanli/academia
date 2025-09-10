package tg.academia.administration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.academia.administration.entity.Attendance;
import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentIdAndDateBetween(Long studentId, LocalDate start, LocalDate end);
    List<Attendance> findByDate(LocalDate date);
}
