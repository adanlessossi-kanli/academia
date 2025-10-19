package tg.academia.administration.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import tg.academia.administration.entity.SchoolClass;
import tg.academia.administration.entity.Student;
import tg.academia.administration.entity.Teacher;
import tg.academia.administration.repository.SchoolClassRepository;
import tg.academia.administration.repository.StudentRepository;
import tg.academia.administration.repository.TeacherRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureHttpGraphQlTester
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=create-drop", "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration"})
public abstract class BaseGraphQLIntegrationTest {

    @Autowired
    protected TeacherRepository teacherRepository;

    @Autowired
    protected SchoolClassRepository schoolClassRepository;

    @Autowired
    protected StudentRepository studentRepository;

    @BeforeEach
    void setupTestData() {
        String suffix = String.valueOf(System.currentTimeMillis());
        
        Teacher teacher1 = new Teacher();
        teacher1.setFirstName("John");
        teacher1.setLastName("Smith");
        teacher1.setEmail("john.smith" + suffix + "@school.com");
        teacher1.setSubject("Mathematics");
        teacherRepository.save(teacher1);

        Teacher teacher2 = new Teacher();
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Doe");
        teacher2.setEmail("jane.doe" + suffix + "@school.com");
        teacher2.setSubject("Science");
        teacherRepository.save(teacher2);

        SchoolClass class1 = new SchoolClass();
        class1.setName("Math Class");
        class1.setRoom("Room 101");
        class1.setGrade(1);
        class1.setTeacher(teacher1);
        schoolClassRepository.save(class1);

        SchoolClass class2 = new SchoolClass();
        class2.setName("Science Class");
        class2.setRoom("Room 102");
        class2.setGrade(2);
        class2.setTeacher(teacher2);
        schoolClassRepository.save(class2);

        Student student1 = new Student();
        student1.setFirstName("Alice");
        student1.setLastName("Johnson");
        student1.setGrade(1);
        student1.setEmail("alice" + suffix + "@example.com");
        student1.setSchoolClass(class1);
        studentRepository.save(student1);

        Student student2 = new Student();
        student2.setFirstName("Bob");
        student2.setLastName("Wilson");
        student2.setGrade(2);
        student2.setEmail("bob" + suffix + "@example.com");
        student2.setSchoolClass(class2);
        studentRepository.save(student2);
    }
}