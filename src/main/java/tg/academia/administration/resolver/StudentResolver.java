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

import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.toList;

@Controller
@RequiredArgsConstructor
public class StudentResolver {
    private final StudentRepository studentRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final StudentService studentService;

    @QueryMapping
    public List<Student> students(@Argument Integer limit) {
        return limit != null 
            ? studentRepository.findAll(PageRequest.of(0, limit)).getContent()
            : studentRepository.findAll();
    }

    @QueryMapping
    public List<Student> studentsByGrade(@Argument Integer grade) {
        return studentService.getStudentsByGrade(grade);
    }
    
    @QueryMapping
    public List<Student> searchStudents(@Argument String name) {
        return studentRepository.searchByName(name);
    }

    @MutationMapping
    public Student createStudent(
            @Argument String firstName, 
            @Argument String lastName, 
            @Argument Integer grade, 
            @Argument String email, 
            @Argument Long classId) {
        return studentService.createStudent(firstName, lastName, grade, email, classId);
    }
    
    @BatchMapping
    public Map<Student, SchoolClass> schoolClass(List<Student> students) {
        var classIds = students.stream()
            .filter(s -> s.getSchoolClass() != null)
            .map(s -> s.getSchoolClass().getId())
            .distinct()
            .collect(toList());
        
        if (classIds.isEmpty()) {
            return Map.of();
        }
        
        var classes = schoolClassRepository.findAllById(classIds)
            .stream()
            .collect(toMap(SchoolClass::getId, c -> c));
        
        return students.stream()
            .filter(s -> s.getSchoolClass() != null && classes.containsKey(s.getSchoolClass().getId()))
            .collect(toMap(s -> s, s -> classes.get(s.getSchoolClass().getId())));
    }
}
