package tg.academia.administration.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "students", 
       uniqueConstraints = @UniqueConstraint(columnNames = "email"),
       indexes = {
           @Index(name = "idx_student_grade", columnList = "grade"),
           @Index(name = "idx_student_email", columnList = "email"),
           @Index(name = "idx_student_class", columnList = "class_id"),
           @Index(name = "idx_student_name", columnList = "lastName, firstName")
       })
@EntityListeners(AuditingEntityListener.class)
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "First name is required")
    @Column(nullable = false)
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Column(nullable = false)
    private String lastName;
    
    @Min(value = 1, message = "Grade must be between 1 and 6")
    @Max(value = 6, message = "Grade must be between 1 and 6")
    @Column(nullable = false)
    private Integer grade;
    
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Column(unique = true, nullable = false)
    private String email;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    @JsonIgnore
    private SchoolClass schoolClass;
    
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public boolean isInGrade(int targetGrade) {
        return grade != null && grade == targetGrade;
    }
}
