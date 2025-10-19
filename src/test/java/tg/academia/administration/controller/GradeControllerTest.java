package tg.academia.administration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import tg.academia.administration.config.TestSecurityConfig;
import tg.academia.administration.entity.Grade;
import tg.academia.administration.entity.Student;
import tg.academia.administration.repository.GradeRepository;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GradeController.class)
@Import(TestSecurityConfig.class)
class GradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GradeRepository gradeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void getGradesByStudent_ShouldReturnGrades() throws Exception {
        Student student = new Student();
        student.setId(1L);
        
        Grade grade1 = new Grade();
        grade1.setId(1L);
        grade1.setStudent(student);
        grade1.setSubject("Math");
        grade1.setScore(85.0);
        
        Grade grade2 = new Grade();
        grade2.setId(2L);
        grade2.setStudent(student);
        grade2.setSubject("Science");
        grade2.setScore(92.0);
        
        List<Grade> grades = Arrays.asList(grade1, grade2);
        when(gradeRepository.findByStudentId(1L)).thenReturn(grades);

        mockMvc.perform(get("/api/grades/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].subject").value("Math"))
                .andExpect(jsonPath("$[0].score").value(85.0))
                .andExpect(jsonPath("$[1].subject").value("Science"))
                .andExpect(jsonPath("$[1].score").value(92.0));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void createGrade_WithValidData_ShouldCreateGrade() throws Exception {
        GradeController.CreateGradeRequest request = new GradeController.CreateGradeRequest(
                1L, "Math", "Fall 2024", 88.5
        );

        Grade savedGrade = new Grade();
        savedGrade.setId(1L);
        Student student = new Student();
        student.setId(1L);
        savedGrade.setStudent(student);
        savedGrade.setSubject("Math");
        savedGrade.setSemester("Fall 2024");
        savedGrade.setScore(88.5);

        when(gradeRepository.save(any(Grade.class))).thenReturn(savedGrade);

        mockMvc.perform(post("/api/grades")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.subject").value("Math"))
                .andExpect(jsonPath("$.semester").value("Fall 2024"))
                .andExpect(jsonPath("$.score").value(88.5));
    }

    @Test
    void createGrade_WithoutAuthentication_ShouldReturn401() throws Exception {
        GradeController.CreateGradeRequest request = new GradeController.CreateGradeRequest(
                1L, "Math", "Fall 2024", 88.5
        );

        mockMvc.perform(post("/api/grades")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getGradesByStudent_WithoutAuthentication_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/grades/student/1"))
                .andExpect(status().isUnauthorized());
    }
}