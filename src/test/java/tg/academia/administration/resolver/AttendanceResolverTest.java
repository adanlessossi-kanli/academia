package tg.academia.administration.resolver;

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
class AttendanceResolverTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private AttendanceResolver attendanceResolver;

    @Test
    void attendanceByDate_ReturnsAttendanceForDate() {
        Attendance attendance = new Attendance();
        attendance.setDate(LocalDate.parse("2024-01-15"));
        when(attendanceRepository.findByDate(LocalDate.parse("2024-01-15")))
            .thenReturn(List.of(attendance));

        List<Attendance> result = attendanceResolver.attendanceByDate("2024-01-15");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDate()).isEqualTo(LocalDate.parse("2024-01-15"));
    }

    @Test
    void markAttendance_CreatesNewAttendance() {
        Student student = new Student();
        student.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(attendanceRepository.save(any(Attendance.class))).thenAnswer(i -> i.getArgument(0));

        Attendance result = attendanceResolver.markAttendance(1L, "2024-01-15", "PRESENT");

        assertThat(result.getStudent()).isEqualTo(student);
        assertThat(result.getStatus()).isEqualTo(Attendance.AttendanceStatus.PRESENT);
    }
}
