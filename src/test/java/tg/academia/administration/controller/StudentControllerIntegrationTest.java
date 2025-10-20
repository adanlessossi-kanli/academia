package tg.academia.administration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.transaction.annotation.Transactional;
import tg.academia.administration.dto.StudentRequest;
import tg.academia.administration.entity.Student;
import tg.academia.administration.repository.StudentRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@org.springframework.context.annotation.Import(tg.academia.administration.config.TestSecurityConfig.class)
@Transactional
class StudentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateStudent() throws Exception {
        var request = new StudentRequest(
                "John", "Doe", 1, "john.doe@test.com", null);

        mockMvc.perform(post("/api/students")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@test.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetStudentsWithPagination() throws Exception {
        // Create test data
        Student student1 = new Student();
        student1.setFirstName("Alice");
        student1.setLastName("Smith");
        student1.setGrade(1);
        student1.setEmail("alice@test.com");
        studentRepository.save(student1);

        Student student2 = new Student();
        student2.setFirstName("Bob");
        student2.setLastName("Jones");
        student2.setGrade(2);
        student2.setEmail("bob@test.com");
        studentRepository.save(student2);

        mockMvc.perform(get("/api/students")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "firstName")
                .param("sortDir", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content[0].firstName").value("Alice"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetStudentById() throws Exception {
        Student student = new Student();
        student.setFirstName("Test");
        student.setLastName("User");
        student.setGrade(1);
        student.setEmail("test@test.com");
        Student saved = studentRepository.save(student);

        mockMvc.perform(get("/api/students/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturn404ForNonExistentStudent() throws Exception {
        mockMvc.perform(get("/api/students/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource Not Found"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateStudent() throws Exception {
        Student student = new Student();
        student.setFirstName("Original");
        student.setLastName("Name");
        student.setGrade(1);
        student.setEmail("original@test.com");
        Student saved = studentRepository.save(student);

        var updateRequest = new StudentRequest(
                "Updated", "Name", 1, "updated@test.com", null);

        mockMvc.perform(put("/api/students/" + saved.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.email").value("updated@test.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteStudent() throws Exception {
        Student student = new Student();
        student.setFirstName("ToDelete");
        student.setLastName("User");
        student.setGrade(1);
        student.setEmail("delete@test.com");
        Student saved = studentRepository.save(student);

        mockMvc.perform(delete("/api/students/" + saved.getId())
                .with(csrf()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/students/" + saved.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRequireAuthenticationForProtectedEndpoints() throws Exception {
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void shouldForbidDeleteForNonAdminUsers() throws Exception {
        Student student = new Student();
        student.setFirstName("Test");
        student.setLastName("User");
        student.setGrade(1);
        student.setEmail("forbid@test.com");
        Student saved = studentRepository.save(student);
        
        mockMvc.perform(delete("/api/students/" + saved.getId())
                .with(csrf()))
                .andExpect(status().isForbidden());
    }
}