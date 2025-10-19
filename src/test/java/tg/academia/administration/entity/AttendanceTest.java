package tg.academia.administration.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class AttendanceTest {

    @Test
    void shouldCreateAttendanceWithValidData() {
        Attendance attendance = new Attendance();
        LocalDate date = LocalDate.now();
        attendance.setDate(date);
        attendance.setStatus(Attendance.AttendanceStatus.PRESENT);

        assertEquals(date, attendance.getDate());
        assertEquals(Attendance.AttendanceStatus.PRESENT, attendance.getStatus());
    }

    @Test
    void shouldHaveAllAttendanceStatuses() {
        assertEquals(4, Attendance.AttendanceStatus.values().length);
        assertTrue(java.util.Arrays.asList(Attendance.AttendanceStatus.values())
                .contains(Attendance.AttendanceStatus.PRESENT));
        assertTrue(java.util.Arrays.asList(Attendance.AttendanceStatus.values())
                .contains(Attendance.AttendanceStatus.ABSENT));
        assertTrue(java.util.Arrays.asList(Attendance.AttendanceStatus.values())
                .contains(Attendance.AttendanceStatus.LATE));
        assertTrue(java.util.Arrays.asList(Attendance.AttendanceStatus.values())
                .contains(Attendance.AttendanceStatus.EXCUSED));
    }
}