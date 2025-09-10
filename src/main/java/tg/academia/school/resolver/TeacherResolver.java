package tg.academia.school.resolver;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import tg.academia.school.entity.Teacher;
import tg.academia.school.repository.TeacherRepository;
import tg.academia.school.service.TeacherService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TeacherResolver {
    private final TeacherRepository teacherRepository;
    private final TeacherService teacherService;

    @QueryMapping
    public List<Teacher> teachers() {
        return teacherRepository.findAll();
    }
    
    @QueryMapping
    public List<Teacher> searchTeachers(@Argument String name) {
        return teacherRepository.searchByName(name);
    }

    @MutationMapping
    public Teacher createTeacher(@Argument String firstName, @Argument String lastName, 
                                @Argument String email, @Argument String subject) {
        try {
            return teacherService.createTeacher(firstName, lastName, email, subject);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}