package tg.academia.school.resolver;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import tg.academia.school.entity.SchoolClass;
import tg.academia.school.repository.SchoolClassRepository;
import tg.academia.school.repository.TeacherRepository;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SchoolClassResolver {
    private final SchoolClassRepository schoolClassRepository;
    private final TeacherRepository teacherRepository;

    @QueryMapping
    public List<SchoolClass> classes() {
        return schoolClassRepository.findAll();
    }

    @QueryMapping
    public List<SchoolClass> classesByGrade(@Argument Integer grade) {
        return schoolClassRepository.findByGrade(grade);
    }

    @MutationMapping
    public SchoolClass createClass(@Argument String name, @Argument Integer grade, 
                                  @Argument String room, @Argument Long teacherId) {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName(name);
        schoolClass.setGrade(grade);
        schoolClass.setRoom(room);
        if (teacherId != null) {
            schoolClass.setTeacher(teacherRepository.findById(teacherId).orElse(null));
        }
        return schoolClassRepository.save(schoolClass);
    }
}