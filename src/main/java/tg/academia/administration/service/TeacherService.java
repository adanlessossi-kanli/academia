package tg.academia.administration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tg.academia.administration.entity.Teacher;
import tg.academia.administration.repository.TeacherRepository;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;

    public Teacher createTeacher(String firstName, String lastName, String email, String subject) {
        if (teacherRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        Teacher teacher = new Teacher();
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        teacher.setEmail(email);
        teacher.setSubject(subject);
        
        return teacherRepository.save(teacher);
    }
}
