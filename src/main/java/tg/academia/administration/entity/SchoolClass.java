package tg.academia.administration.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String name;
    
    @Min(1) @Max(6)
    private Integer grade;
    
    @NotBlank
    private String room;
    
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    
    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Student> students;
}
