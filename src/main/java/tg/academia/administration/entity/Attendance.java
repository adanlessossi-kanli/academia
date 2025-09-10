package tg.academia.administration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    
    @NotNull
    private LocalDate date;
    
    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;
    
    public enum AttendanceStatus {
        PRESENT, ABSENT, LATE, EXCUSED
    }
}
