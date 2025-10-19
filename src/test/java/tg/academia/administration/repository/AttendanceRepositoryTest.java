package tg.academia.administration.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import tg.academia.administration.config.AuditConfig;
import tg.academia.administration.entity.Attendance;
import tg.academia.administration.entity.Student;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(AuditConfig.class)
class AttendanceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Test
    void findByStudentIdAndDateBetween_ReturnsAttendanceInRange() {
        Student student = new Student();
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setGrade(1);
        student.setEmail("john@test.com");
        student = entityManager.persistAndFlush(student);

        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setDate(LocalDate.of(2024, 1, 15));
        attendance.setStatus(Attendance.AttendanceStatus.PRESENT);
        entityManager.persistAndFlush(attendance);

        var result = attendanceRepository.findByStudentIdAndDateBetween(
            student.getId(), LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(Attendance.AttendanceStatus.PRESENT);
    }

    @Test
    void findByDate_ReturnsAttendanceForSpecificDate() {
        Student student = new Student();
        student.setFirstName("Jane");
        student.setLastName("Smith");
        student.setGrade(2);
        student.setEmail("jane@test.com");
        student = entityManager.persistAndFlush(student);

        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setDate(LocalDate.of(2024, 2, 1));
        attendance.setStatus(Attendance.AttendanceStatus.LATE);
        entityManager.persistAndFlush(attendance);

        var result = attendanceRepository.findByDate(LocalDate.of(2024, 2, 1));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(Attendance.AttendanceStatus.LATE);
    }
}
