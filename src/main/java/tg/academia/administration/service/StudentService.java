package tg.academia.administration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import tg.academia.administration.entity.SchoolClass;
import tg.academia.administration.entity.Student;
import tg.academia.administration.exception.DuplicateResourceException;
import tg.academia.administration.exception.ResourceNotFoundException;
import tg.academia.administration.repository.StudentRepository;
import tg.academia.administration.repository.SchoolClassRepository;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final SchoolClassRepository schoolClassRepository;

    public Student createStudent(String firstName, String lastName, Integer grade, String email, Long classId) {
        if (studentRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Student", "email", email);
        }
        
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setGrade(grade);
        student.setEmail(email);
        
        if (classId != null) {
            SchoolClass schoolClass = schoolClassRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("SchoolClass", classId));
            if (!schoolClass.getGrade().equals(grade)) {
                throw new IllegalArgumentException("Student grade must match class grade");
            }
            student.setSchoolClass(schoolClass);
        }
        
        try {
            return studentRepository.save(student);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("Student with email already exists: " + email);
        }
    }
    
    public Student updateStudent(Long id, String firstName, String lastName, Integer grade, String email, Long classId) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
        
        // Check for duplicate email (excluding current student)
        if (!student.getEmail().equals(email) && studentRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Student", "email", email);
        }
        
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setGrade(grade);
        student.setEmail(email);
        
        if (classId != null) {
            SchoolClass schoolClass = schoolClassRepository.findById(classId)
                    .orElseThrow(() -> new ResourceNotFoundException("SchoolClass", classId));
            student.setSchoolClass(schoolClass);
        } else {
            student.setSchoolClass(null);
        }
        
        return studentRepository.save(student);
    }
}
