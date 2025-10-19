package tg.academia.administration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tg.academia.administration.controller.BulkOperationsController.BulkAttendanceRequest;
import tg.academia.administration.entity.Attendance;
import tg.academia.administration.entity.Student;
import tg.academia.administration.repository.AttendanceRepository;
import tg.academia.administration.repository.StudentRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BulkOperationsService {

    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;

    public Map<String, Object> importStudents(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int errorCount = 0;
        List<String> errors = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 4) {
                    try {
                        Student student = new Student();
                        student.setFirstName(fields[0].trim());
                        student.setLastName(fields[1].trim());
                        student.setGrade(Integer.parseInt(fields[2].trim()));
                        student.setEmail(fields[3].trim());
                        
                        studentRepository.save(student);
                        successCount++;
                    } catch (Exception e) {
                        errorCount++;
                        errors.add("Line " + (successCount + errorCount) + ": " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error importing students", e);
            errors.add("File processing error: " + e.getMessage());
        }

        result.put("successCount", successCount);
        result.put("errorCount", errorCount);
        result.put("errors", errors);
        return result;
    }

    public byte[] exportStudents() {
        List<Student> students = studentRepository.findAll();
        StringBuilder csv = new StringBuilder();
        csv.append("First Name,Last Name,Grade,Email\n");
        
        for (Student student : students) {
            csv.append(student.getFirstName()).append(",")
               .append(student.getLastName()).append(",")
               .append(student.getGrade()).append(",")
               .append(student.getEmail()).append("\n");
        }
        
        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    public Map<String, Object> markBulkAttendance(BulkAttendanceRequest request) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int errorCount = 0;
        List<String> errors = new ArrayList<>();

        for (Long studentId : request.studentIds()) {
            try {
                Optional<Student> studentOpt = studentRepository.findById(studentId);
                if (studentOpt.isPresent()) {
                    Attendance attendance = new Attendance();
                    attendance.setStudent(studentOpt.get());
                    attendance.setDate(request.date());
                    attendance.setStatus(Attendance.AttendanceStatus.valueOf(request.status()));
                    
                    attendanceRepository.save(attendance);
                    successCount++;
                } else {
                    errorCount++;
                    errors.add("Student not found: " + studentId);
                }
            } catch (Exception e) {
                errorCount++;
                errors.add("Error for student " + studentId + ": " + e.getMessage());
            }
        }

        result.put("successCount", successCount);
        result.put("errorCount", errorCount);
        result.put("errors", errors);
        return result;
    }
}