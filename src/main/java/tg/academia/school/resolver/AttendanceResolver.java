package tg.academia.school.resolver;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import tg.academia.school.entity.Attendance;
import tg.academia.school.repository.AttendanceRepository;
import tg.academia.school.repository.StudentRepository;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AttendanceResolver {
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    @QueryMapping
    public List<Attendance> attendanceByDate(@Argument String date) {
        return attendanceRepository.findByDate(LocalDate.parse(date));
    }

    @QueryMapping
    public List<Attendance> studentAttendance(@Argument Long studentId, @Argument String startDate, @Argument String endDate) {
        return attendanceRepository.findByStudentIdAndDateBetween(
            studentId, LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    @MutationMapping
    public Attendance markAttendance(@Argument Long studentId, @Argument String date, @Argument String status) {
        Attendance attendance = new Attendance();
        attendance.setStudent(studentRepository.findById(studentId).orElseThrow());
        attendance.setDate(LocalDate.parse(date));
        attendance.setStatus(Attendance.AttendanceStatus.valueOf(status));
        return attendanceRepository.save(attendance);
    }
}