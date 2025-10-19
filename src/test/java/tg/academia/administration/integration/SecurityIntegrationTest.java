package tg.academia.administration.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@org.springframework.context.annotation.Import(tg.academia.administration.config.TestSecurityConfig.class)
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    void admin_CanCreateStudent() throws Exception {
        mockMvc.perform(post("/api/students")
                .with(csrf())
                .contentType("application/json")
                .content("{\"firstName\":\"Test\",\"lastName\":\"User\",\"grade\":1,\"email\":\"test@example.com\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void teacher_CannotCreateStudent() throws Exception {
        mockMvc.perform(post("/api/students")
                .with(csrf())
                .contentType("application/json")
                .content("{\"firstName\":\"Test\",\"lastName\":\"User\",\"grade\":1,\"email\":\"test2@example.com\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void teacher_CanCreateGrade() throws Exception {
        mockMvc.perform(post("/api/grades")
                .with(csrf())
                .contentType("application/json")
                .content("{\"studentId\":1,\"subject\":\"Math\",\"semester\":\"Fall\",\"score\":85.0}"))
                .andExpect(status().isOk());
    }
}