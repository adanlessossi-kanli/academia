package tg.academia.administration.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Async
    public void sendStudentEnrollmentNotification(String email, String studentName) {
        if (mailSender == null) {
            log.warn("Mail service not available. Skipping enrollment notification for: {}", email);
            return;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Student Enrollment Confirmation");
            message.setText("Dear Parent/Guardian,\n\n" +
                    "We are pleased to confirm that " + studentName + " has been successfully enrolled.\n\n" +
                    "Best regards,\nAcademia Administration");
            
            mailSender.send(message);
            log.info("Enrollment notification sent to: {}", email);
        } catch (Exception e) {
            log.error("Failed to send enrollment notification to: {}", email, e);
        }
    }

    @Async
    public void sendAttendanceAlert(String email, String studentName, String date) {
        if (mailSender == null) {
            log.warn("Mail service not available. Skipping attendance alert for: {}", email);
            return;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Attendance Alert");
            message.setText("Dear Parent/Guardian,\n\n" +
                    studentName + " was marked absent on " + date + ".\n\n" +
                    "Best regards,\nAcademia Administration");
            
            mailSender.send(message);
            log.info("Attendance alert sent to: {}", email);
        } catch (Exception e) {
            log.error("Failed to send attendance alert to: {}", email, e);
        }
    }
}