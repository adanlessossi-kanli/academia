package tg.academia.administration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
@Table(name = "grades")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    
    @NotBlank(message = "Subject is required")
    @Column(nullable = false)
    private String subject;
    
    @DecimalMin(value = "0.0", message = "Score must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Score must be between 0 and 100")
    @Column(nullable = false)
    private Double score;
    
    @NotBlank(message = "Semester is required")
    @Column(nullable = false)
    private String semester;
}
