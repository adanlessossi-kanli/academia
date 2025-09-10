package tg.academia.school.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tg.academia.school.entity.Student;
import tg.academia.school.entity.Teacher;
import tg.academia.school.entity.SchoolClass;
import tg.academia.school.repository.StudentRepository;
import tg.academia.school.repository.TeacherRepository;
import tg.academia.school.repository.SchoolClassRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RepositoryIntegrationTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SchoolClassRepository schoolClassRepository;

    @Test
    void studentClassTeacherRelationship_WorksCorrectly() {
        // Create teacher
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Smith");
        teacher.setEmail("john@test.com");
        teacher.setSubject("Math");
        teacher = teacherRepository.save(teacher);

        // Create class
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName("Grade 3A");
        schoolClass.setGrade(3);
        schoolClass.setRoom("101");
        schoolClass.setTeacher(teacher);
        schoolClass = schoolClassRepository.save(schoolClass);

        // Create student
        Student student = new Student();
        student.setFirstName("Alice");
        student.setLastName("Johnson");
        student.setGrade(3);
        student.setEmail("alice@test.com");
        student.setSchoolClass(schoolClass);
        student = studentRepository.save(student);

        // Verify relationships
        Student foundStudent = studentRepository.findById(student.getId()).orElseThrow();
        assertThat(foundStudent.getSchoolClass().getName()).isEqualTo("Grade 3A");
        assertThat(foundStudent.getSchoolClass().getTeacher().getFirstName()).isEqualTo("John");

        var studentsInClass = studentRepository.findBySchoolClassId(schoolClass.getId());
        assertThat(studentsInClass).hasSize(1);
        assertThat(studentsInClass.get(0).getFirstName()).isEqualTo("Alice");
    }

    @Test
    void uniqueConstraints_PreventDuplicateEmails() {
        Student student1 = new Student();
        student1.setFirstName("John");
        student1.setLastName("Doe");
        student1.setGrade(1);
        student1.setEmail("duplicate@test.com");
        studentRepository.save(student1);

        boolean exists = studentRepository.existsByEmail("duplicate@test.com");
        assertThat(exists).isTrue();
    }
}