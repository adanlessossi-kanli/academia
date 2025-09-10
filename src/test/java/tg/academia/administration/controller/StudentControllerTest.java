package tg.academia.administration.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tg.academia.administration.entity.Student;
import tg.academia.administration.repository.StudentRepository;
import tg.academia.administration.service.StudentService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    void getAllStudents_ReturnsAllStudents() {
        Student student = new Student();
        student.setFirstName("John");
        when(studentRepository.findAll()).thenReturn(List.of(student));

        List<Student> result = studentController.getAllStudents();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    void getStudentsByGrade_ReturnsStudentsForGrade() {
        Student student = new Student();
        student.setGrade(3);
        when(studentRepository.findByGrade(3)).thenReturn(List.of(student));

        List<Student> result = studentController.getStudentsByGrade(3);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getGrade()).isEqualTo(3);
    }

    @Test
    void searchStudents_ReturnsMatchingStudents() {
        Student student = new Student();
        student.setFirstName("Alice");
        when(studentRepository.searchByName("Ali")).thenReturn(List.of(student));

        List<Student> result = studentController.searchStudents("Ali");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("Alice");
    }

    @Test
    void createStudent_ReturnsCreatedStudent() {
        Student student = new Student();
        student.setFirstName("Jane");
        var request = new StudentController.CreateStudentRequest("Jane", "Doe", 2, "jane@test.com", null);
        when(studentService.createStudent("Jane", "Doe", 2, "jane@test.com", null)).thenReturn(student);

        Student result = studentController.createStudent(request);

        assertThat(result.getFirstName()).isEqualTo("Jane");
    }
}
