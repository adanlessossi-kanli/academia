package tg.academia.administration.mapper;

import tg.academia.administration.dto.StudentResponse;
import tg.academia.administration.entity.Student;

public class StudentMapper {
    
    public static StudentResponse toResponse(Student student) {
        return new StudentResponse(
            student.getId(),
            student.getFirstName(),
            student.getLastName(),
            student.getFullName(),
            student.getGrade(),
            student.getEmail(),
            student.getSchoolClass() != null ? student.getSchoolClass().getId() : null,
            student.getSchoolClass() != null ? student.getSchoolClass().getName() : null,
            student.getCreatedAt(),
            student.getUpdatedAt()
        );
    }
}
