package tg.academia.administration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tg.academia.administration.entity.Student;
import tg.academia.administration.repository.StudentRepository;
import tg.academia.administration.repository.SchoolClassRepository;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final SchoolClassRepository schoolClassRepository;

    public Student createStudent(String firstName, String lastName, Integer grade, String email, Long classId) {
        if (studentRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setGrade(grade);
        student.setEmail(email);
        
        if (classId != null) {
            var schoolClass = schoolClassRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));
            if (!schoolClass.getGrade().equals(grade)) {
                throw new IllegalArgumentException("Student grade must match class grade");
            }
            student.setSchoolClass(schoolClass);
        }
        
        return studentRepository.save(student);
    }
}
