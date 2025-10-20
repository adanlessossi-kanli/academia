package tg.academia.administration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tg.academia.administration.entity.Teacher;
import tg.academia.administration.exception.DuplicateResourceException;
import tg.academia.administration.repository.TeacherRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherService {
    private final TeacherRepository teacherRepository;

    @Transactional
    public Teacher createTeacher(String firstName, String lastName, String email, String subject) {
        if (teacherRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Teacher", "email", email);
        }
        
        var teacher = new Teacher();
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        teacher.setEmail(email);
        teacher.setSubject(subject);
        
        return teacherRepository.save(teacher);
    }
}
