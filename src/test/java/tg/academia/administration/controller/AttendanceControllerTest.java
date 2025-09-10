package tg.academia.administration.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tg.academia.administration.entity.Attendance;
import tg.academia.administration.entity.Student;
import tg.academia.administration.repository.AttendanceRepository;
import tg.academia.administration.repository.StudentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendanceControllerTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private AttendanceController attendanceController;

    @Test
    void getAttendanceByDate_ReturnsAttendanceForDate() {
        Attendance attendance = new Attendance();
        attendance.setDate(LocalDate.parse("2024-01-15"));
        when(attendanceRepository.findByDate(LocalDate.parse("2024-01-15"))).thenReturn(List.of(attendance));

        List<Attendance> result = attendanceController.getAttendanceByDate("2024-01-15");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDate()).isEqualTo(LocalDate.parse("2024-01-15"));
    }

    @Test
    void getStudentAttendance_ReturnsStudentAttendanceHistory() {
        Attendance attendance = new Attendance();
        when(attendanceRepository.findByStudentIdAndDateBetween(1L, LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-31")))
            .thenReturn(List.of(attendance));

        List<Attendance> result = attendanceController.getStudentAttendance(1L, "2024-01-01", "2024-01-31");

        assertThat(result).hasSize(1);
    }

    @Test
    void markAttendance_CreatesAttendanceRecord() {
        Student student = new Student();
        student.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(attendanceRepository.save(any(Attendance.class))).thenAnswer(i -> i.getArgument(0));

        var request = new AttendanceController.MarkAttendanceRequest(1L, "2024-01-15", "PRESENT");
        Attendance result = attendanceController.markAttendance(request);

        assertThat(result.getStudent()).isEqualTo(student);
        assertThat(result.getStatus()).isEqualTo(Attendance.AttendanceStatus.PRESENT);
    }
}
