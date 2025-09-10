package tg.academia.school.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String firstName;
    
    @NotBlank
    private String lastName;
    
    @Email
    @Column(unique = true)
    private String email;
    
    @NotBlank
    private String subject;
    
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<SchoolClass> classes;
}