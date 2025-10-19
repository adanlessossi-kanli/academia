package tg.academia.administration.service;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@org.springframework.context.annotation.Import({tg.academia.administration.config.TestSecurityConfig.class, NotificationServiceTest.TestMailConfig.class})
class NotificationServiceTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("test", "test"));

    @Autowired
    private NotificationService notificationService;

    @TestConfiguration
    static class TestMailConfig {
        @Bean
        @Primary
        public JavaMailSender javaMailSender() {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("localhost");
            mailSender.setPort(3025);
            mailSender.setProtocol("smtp");
            mailSender.getJavaMailProperties().put("mail.smtp.auth", false);
            mailSender.getJavaMailProperties().put("mail.smtp.starttls.enable", false);
            mailSender.getJavaMailProperties().put("mail.smtp.ssl.enable", false);
            mailSender.getJavaMailProperties().put("mail.debug", false);
            return mailSender;
        }
    }

    @Test
    void shouldSendEnrollmentNotification() throws Exception {
        // Given
        greenMail.reset();
        String email = "parent@test.com";
        String studentName = "John Doe";

        // When
        notificationService.sendStudentEnrollmentNotification(email, studentName);

        // Wait for async processing
        Thread.sleep(2000);

        // Then
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(1, receivedMessages.length);
        
        MimeMessage message = receivedMessages[0];
        assertEquals("Student Enrollment Confirmation", message.getSubject());
        assertTrue(message.getContent().toString().contains(studentName));
        assertEquals(email, message.getAllRecipients()[0].toString());
    }

    @Test
    void shouldSendAttendanceAlert() throws Exception {
        // Given
        greenMail.reset();
        String email = "parent@test.com";
        String studentName = "Jane Smith";
        String date = "2024-01-15";

        // When
        notificationService.sendAttendanceAlert(email, studentName, date);

        // Wait for async processing
        Thread.sleep(2000);

        // Then
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(1, receivedMessages.length);
        
        MimeMessage message = receivedMessages[0];
        assertEquals("Attendance Alert", message.getSubject());
        assertTrue(message.getContent().toString().contains(studentName));
        assertTrue(message.getContent().toString().contains(date));
        assertEquals(email, message.getAllRecipients()[0].toString());
    }

    @Test
    void shouldHandleMultipleNotifications() throws Exception {
        // Given
        greenMail.reset();

        // When
        notificationService.sendStudentEnrollmentNotification("parent1@test.com", "Student 1");
        notificationService.sendAttendanceAlert("parent2@test.com", "Student 2", "2024-01-15");

        // Wait for async processing
        Thread.sleep(2000);

        // Then
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(2, receivedMessages.length);
    }
}