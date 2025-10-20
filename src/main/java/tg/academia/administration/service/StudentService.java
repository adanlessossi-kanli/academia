package tg.academia.administration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tg.academia.administration.entity.Student;
import tg.academia.administration.exception.DuplicateResourceException;
import tg.academia.administration.exception.ResourceNotFoundException;
import tg.academia.administration.repository.StudentRepository;
import tg.academia.administration.repository.SchoolClassRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {
    private final StudentRepository studentRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final CacheManager cacheManager;

    @Transactional
    @CacheEvict(value = "students", key = "#grade")
    public Student createStudent(String firstName, String lastName, Integer grade, String email, Long classId) {
        validateEmailUniqueness(email);
        
        var schoolClass = classId != null ? validateAndGetSchoolClass(classId, grade) : null;
        
        var student = Student.builder()
                .firstName(firstName)
                .lastName(lastName)
                .grade(grade)
                .email(email)
                .schoolClass(schoolClass)
                .build();
        
        return studentRepository.save(student);
    }
    
    @Transactional
    @CacheEvict(value = "students", key = "#grade")
    public Student updateStudent(Long id, String firstName, String lastName, Integer grade, String email, Long classId) {
        var student = findStudentById(id);
        Integer oldGrade = student.getGrade();
        
        if (!student.getEmail().equals(email)) {
            validateEmailUniqueness(email);
        }
        
        var schoolClass = classId != null ? validateAndGetSchoolClass(classId, grade) : null;
        
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setGrade(grade);
        student.setEmail(email);
        student.setSchoolClass(schoolClass);
        
        // Evict old grade cache if grade changed
        if (oldGrade != null && !oldGrade.equals(grade)) {
            var cache = cacheManager.getCache("students");
            if (cache != null) {
                cache.evict(oldGrade);
            }
        }
        
        return studentRepository.save(student);
    }
    
    @Cacheable(value = "students", key = "#grade")
    public List<Student> getStudentsByGrade(Integer grade) {
        return studentRepository.findByGradeOrderByLastNameAscFirstNameAsc(grade);
    }
    
    @Cacheable(value = "students", key = "'all'")
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    @Transactional
    public void deleteStudent(Long id) {
        var student = findStudentById(id);
        var cache = cacheManager.getCache("students");
        if (cache != null) {
            cache.evict(student.getGrade());
        }
        studentRepository.deleteById(id);
    }
    
    private void validateEmailUniqueness(String email) {
        if (studentRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Student", "email", email);
        }
    }
    
    private tg.academia.administration.entity.SchoolClass validateAndGetSchoolClass(Long classId, Integer grade) {
        var schoolClass = schoolClassRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("SchoolClass", classId));
        
        if (!schoolClass.getGrade().equals(grade)) {
            throw new IllegalArgumentException("Student grade must match class grade");
        }
        
        return schoolClass;
    }
    
    private Student findStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
    }
}
