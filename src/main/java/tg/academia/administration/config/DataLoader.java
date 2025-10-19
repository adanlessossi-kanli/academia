package tg.academia.administration.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tg.academia.administration.entity.User;
import tg.academia.administration.repository.UserRepository;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            // Create admin user
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@academia.tg");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(User.Role.ADMIN);
            admin.setEnabled(true);
            
            // Create teacher user
            User teacher = new User();
            teacher.setUsername("teacher");
            teacher.setEmail("teacher@academia.tg");
            teacher.setPassword(passwordEncoder.encode("teacher123"));
            teacher.setRole(User.Role.TEACHER);
            teacher.setEnabled(true);
            
            // Create student user
            User student = new User();
            student.setUsername("student");
            student.setEmail("student@academia.tg");
            student.setPassword(passwordEncoder.encode("student123"));
            student.setRole(User.Role.STUDENT);
            student.setEnabled(true);
            
            userRepository.save(admin);
            userRepository.save(teacher);
            userRepository.save(student);
        }
    }
}