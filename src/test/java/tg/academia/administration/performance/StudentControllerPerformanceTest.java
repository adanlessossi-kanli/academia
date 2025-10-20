package tg.academia.administration.performance;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tg.academia.administration.dto.StudentRequest;
import tg.academia.administration.entity.Student;
import tg.academia.administration.repository.StudentRepository;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@org.springframework.context.annotation.Import(tg.academia.administration.config.TestSecurityConfig.class)
@Transactional
class StudentControllerPerformanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldHandleMultipleStudentCreationsEfficiently() throws Exception {
        long startTime = System.nanoTime();
        
        // Create 50 students
        for (int i = 0; i < 50; i++) {
            var request = new StudentRequest(
                    "Student", "Test" + i, 1, "student" + i + "@test.com", null);

            mockMvc.perform(post("/api/students")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }
        
        long duration = System.nanoTime() - startTime;
        long durationMs = TimeUnit.NANOSECONDS.toMillis(duration);
        
        // Should complete within 10 seconds
        assertTrue(durationMs < 10000, "Creating 50 students took " + durationMs + "ms");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldHandlePaginationEfficiently() throws Exception {
        // Create test data
        String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez",
                              "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin",
                              "Lee", "Perez", "Thompson", "White", "Harris"};
        for (int i = 0; i < 25; i++) {
            Student student = Student.builder()
                .firstName("Test")
                .lastName(lastNames[i])
                .grade(1)
                .email("test" + i + "@test.com")
                .build();
            studentRepository.save(student);
        }

        long startTime = System.nanoTime();
        
        // Test pagination performance
        for (int page = 0; page < 3; page++) {
            mockMvc.perform(get("/api/students")
                    .param("page", String.valueOf(page))
                    .param("size", "10"))
                    .andExpect(status().isOk());
        }
        
        long duration = System.nanoTime() - startTime;
        long durationMs = TimeUnit.NANOSECONDS.toMillis(duration);
        
        // Should complete within 2 seconds
        assertTrue(durationMs < 2000, "Pagination requests took " + durationMs + "ms");
    }
}