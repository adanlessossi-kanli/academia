package tg.academia.administration.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import tg.academia.administration.controller.StudentController;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class RestApiIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getAllStudents_ReturnsStudentList() {
        var response = restTemplate.getForEntity("/api/students", Object[].class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void createStudent_ReturnsCreatedStudent() {
        var request = new StudentController.CreateStudentRequest("Test", "User", 3, "test@example.com", null);
        
        var response = restTemplate.postForEntity("/api/students", request, Object.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getAllTeachers_ReturnsTeacherList() {
        var response = restTemplate.getForEntity("/api/teachers", Object[].class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void searchStudents_ReturnsMatchingStudents() {
        var response = restTemplate.getForEntity("/api/students/search?name=Alice", Object[].class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
