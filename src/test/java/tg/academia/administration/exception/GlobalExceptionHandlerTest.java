package tg.academia.administration.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import tg.academia.administration.controller.StudentController;
import tg.academia.administration.repository.StudentRepository;
import tg.academia.administration.service.StudentService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@Import({GlobalExceptionHandler.class, tg.academia.administration.config.TestSecurityConfig.class})
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldHandleResourceNotFoundException() throws Exception {
        when(studentRepository.findById(999L)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/api/students/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource Not Found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldHandleValidationException() throws Exception {
        var request = new StudentController.CreateStudentRequest("", "", 0, "invalid-email", null);

        mockMvc.perform(post("/api/students")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldHandleDuplicateResourceException() throws Exception {
        when(studentService.createStudent(any(), any(), any(), any(), any()))
                .thenThrow(new DuplicateResourceException("Student", "email", "test@test.com"));

        var request = new StudentController.CreateStudentRequest("John", "Doe", 1, "test@test.com", null);

        mockMvc.perform(post("/api/students")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Duplicate Resource"));
    }
}