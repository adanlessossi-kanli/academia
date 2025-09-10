package tg.academia.administration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    
    @NotBlank
    private String subject;
    
    @DecimalMin("0.0") @DecimalMax("100.0")
    private Double score;
    
    @NotBlank
    private String semester;
}
