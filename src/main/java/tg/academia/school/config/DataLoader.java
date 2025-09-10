package tg.academia.school.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tg.academia.school.entity.*;
import tg.academia.school.repository.*;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final TeacherRepository teacherRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final StudentRepository studentRepository;
    private final GradeRepository gradeRepository;
    private final AttendanceRepository attendanceRepository;

    @Override
    public void run(String... args) {
        // Create teachers
        Teacher teacher1 = new Teacher();
        teacher1.setFirstName("John");
        teacher1.setLastName("Smith");
        teacher1.setEmail("john.smith@school.com");
        teacher1.setSubject("Mathematics");
        teacherRepository.save(teacher1);

        Teacher teacher2 = new Teacher();
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Doe");
        teacher2.setEmail("jane.doe@school.com");
        teacher2.setSubject("English");
        teacherRepository.save(teacher2);

        // Create classes
        SchoolClass class1 = new SchoolClass();
        class1.setName("Grade 1A");
        class1.setGrade(1);
        class1.setRoom("101");
        class1.setTeacher(teacher1);
        schoolClassRepository.save(class1);

        SchoolClass class2 = new SchoolClass();
        class2.setName("Grade 2A");
        class2.setGrade(2);
        class2.setRoom("201");
        class2.setTeacher(teacher2);
        schoolClassRepository.save(class2);

        // Create students
        Student student1 = new Student();
        student1.setFirstName("Alice");
        student1.setLastName("Johnson");
        student1.setGrade(1);
        student1.setEmail("alice.johnson@student.com");
        student1.setSchoolClass(class1);
        studentRepository.save(student1);

        Student student2 = new Student();
        student2.setFirstName("Bob");
        student2.setLastName("Wilson");
        student2.setGrade(2);
        student2.setEmail("bob.wilson@student.com");
        student2.setSchoolClass(class2);
        studentRepository.save(student2);

        // Create grades
        Grade grade1 = new Grade();
        grade1.setStudent(student1);
        grade1.setSubject("Mathematics");
        grade1.setScore(85.5);
        grade1.setSemester("Fall 2024");
        gradeRepository.save(grade1);

        Grade grade2 = new Grade();
        grade2.setStudent(student2);
        grade2.setSubject("English");
        grade2.setScore(92.0);
        grade2.setSemester("Fall 2024");
        gradeRepository.save(grade2);
        
        // Create attendance records
        Attendance attendance1 = new Attendance();
        attendance1.setStudent(student1);
        attendance1.setDate(LocalDate.now().minusDays(1));
        attendance1.setStatus(Attendance.AttendanceStatus.PRESENT);
        attendanceRepository.save(attendance1);
        
        Attendance attendance2 = new Attendance();
        attendance2.setStudent(student2);
        attendance2.setDate(LocalDate.now().minusDays(1));
        attendance2.setStatus(Attendance.AttendanceStatus.LATE);
        attendanceRepository.save(attendance2);
    }
}