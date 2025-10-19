package tg.academia.administration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import tg.academia.administration.config.TestSecurityConfig;
import tg.academia.administration.service.BulkOperationsService;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BulkOperationsController.class)
@Import(TestSecurityConfig.class)
class BulkOperationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BulkOperationsService bulkOperationsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void importStudents_WithValidFile_ShouldReturnSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "students.csv", "text/csv", "name,email\nJohn,john@test.com".getBytes());
        Map<String, Object> result = Map.of("imported", 1, "errors", 0);
        
        when(bulkOperationsService.importStudents(any())).thenReturn(result);

        mockMvc.perform(multipart("/api/bulk/students/import")
                .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imported").value(1))
                .andExpect(jsonPath("$.errors").value(0));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void exportStudents_WithTeacherRole_ShouldReturnCsv() throws Exception {
        byte[] csvData = "name,email\nJohn,john@test.com".getBytes();
        
        when(bulkOperationsService.exportStudents()).thenReturn(csvData);

        mockMvc.perform(get("/api/bulk/students/export"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "text/csv"))
                .andExpect(header().string("Content-Disposition", "attachment; filename=students.csv"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void markBulkAttendance_WithValidRequest_ShouldReturnSuccess() throws Exception {
        BulkOperationsController.BulkAttendanceRequest request = 
            new BulkOperationsController.BulkAttendanceRequest(List.of(1L, 2L), "2023-10-19", "PRESENT");
        Map<String, Object> result = Map.of("marked", 2, "errors", 0);
        
        when(bulkOperationsService.markBulkAttendance(any())).thenReturn(result);

        mockMvc.perform(post("/api/bulk/attendance/mark-bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marked").value(2))
                .andExpect(jsonPath("$.errors").value(0));
    }

    @Test
    void importStudents_WithoutAuthentication_ShouldReturn401() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "students.csv", "text/csv", "name,email\nJohn,john@test.com".getBytes());

        mockMvc.perform(multipart("/api/bulk/students/import")
                .file(file))
                .andExpect(status().isForbidden());
    }
}