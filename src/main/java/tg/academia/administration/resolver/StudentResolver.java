package tg.academia.administration.resolver;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import tg.academia.administration.entity.SchoolClass;
import tg.academia.administration.entity.Student;
import tg.academia.administration.repository.SchoolClassRepository;
import tg.academia.administration.repository.StudentRepository;
import tg.academia.administration.service.StudentService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class StudentResolver {
    private final StudentRepository studentRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final StudentService studentService;

    @QueryMapping
    public List<Student> students(@Argument Integer limit) {
        if (limit != null) {
            return studentRepository.findAll(PageRequest.of(0, limit)).getContent();
        }
        return studentRepository.findAll();
    }

    @QueryMapping
    public List<Student> studentsByGrade(@Argument Integer grade) {
        return studentRepository.findByGrade(grade);
    }
    
    @QueryMapping
    public List<Student> searchStudents(@Argument String name) {
        return studentRepository.searchByName(name);
    }

    @MutationMapping
    public Student createStudent(@Argument String firstName, @Argument String lastName, 
                                @Argument Integer grade, @Argument String email, @Argument Long classId) {
        try {
            return studentService.createStudent(firstName, lastName, grade, email, classId);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    @BatchMapping
    public Map<Student, SchoolClass> schoolClass(List<Student> students) {
        var classIds = students.stream()
            .filter(s -> s.getSchoolClass() != null)
            .map(s -> s.getSchoolClass().getId())
            .collect(Collectors.toSet());
        
        var classes = schoolClassRepository.findAllById(classIds)
            .stream().collect(Collectors.toMap(SchoolClass::getId, c -> c));
        
        return students.stream()
            .filter(s -> s.getSchoolClass() != null)
            .collect(Collectors.toMap(s -> s, s -> classes.get(s.getSchoolClass().getId())));
    }
}
