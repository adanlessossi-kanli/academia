package tg.academia.administration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tg.academia.administration.controller.BulkOperationsController.BulkAttendanceRequest;
import tg.academia.administration.entity.Attendance;
import tg.academia.administration.entity.Student;
import tg.academia.administration.repository.AttendanceRepository;
import tg.academia.administration.repository.StudentRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BulkOperationsService {

    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;

    @Transactional
    public Map<String, Object> importStudents(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        List<Student> validStudents = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        int lineNumber = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String[] fields = line.split(",");
                if (fields.length >= 4) {
                    try {
                        Student student = new Student();
                        student.setFirstName(fields[0].trim());
                        student.setLastName(fields[1].trim());
                        student.setGrade(Integer.parseInt(fields[2].trim()));
                        student.setEmail(fields[3].trim());
                        
                        validStudents.add(student);
                    } catch (Exception e) {
                        errors.add("Line " + lineNumber + ": " + e.getMessage());
                    }
                }
            }
            
            // Batch save all valid students
            List<Student> savedStudents = studentRepository.saveAll(validStudents);
            
            result.put("successCount", savedStudents.size());
            result.put("errorCount", errors.size());
            result.put("errors", errors);
            
        } catch (Exception e) {
            log.error("Error importing students", e);
            errors.add("File processing error: " + e.getMessage());
            result.put("successCount", 0);
            result.put("errorCount", errors.size());
            result.put("errors", errors);
        }

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

    @Transactional
    public Map<String, Object> markBulkAttendance(BulkAttendanceRequest request) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        
        try {
            // Batch fetch all students
            List<Student> students = studentRepository.findAllById(request.studentIds());
            Map<Long, Student> studentMap = students.stream()
                .collect(Collectors.toMap(Student::getId, s -> s));
            
            List<Attendance> attendanceRecords = new ArrayList<>();
            LocalDate date = LocalDate.parse(request.date());
            Attendance.AttendanceStatus status = Attendance.AttendanceStatus.valueOf(request.status());
            
            for (Long studentId : request.studentIds()) {
                Student student = studentMap.get(studentId);
                if (student != null) {
                    Attendance attendance = new Attendance();
                    attendance.setStudent(student);
                    attendance.setDate(date);
                    attendance.setStatus(status);
                    attendanceRecords.add(attendance);
                } else {
                    errors.add("Student not found: " + studentId);
                }
            }
            
            // Batch save all attendance records
            List<Attendance> savedRecords = attendanceRepository.saveAll(attendanceRecords);
            
            result.put("successCount", savedRecords.size());
            result.put("errorCount", errors.size());
            result.put("errors", errors);
            
        } catch (Exception e) {
            log.error("Error marking bulk attendance", e);
            errors.add("Processing error: " + e.getMessage());
            result.put("successCount", 0);
            result.put("errorCount", errors.size());
            result.put("errors", errors);
        }

        return result;
    }
}