package tg.academia.administration.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tg.academia.administration.entity.User;
import tg.academia.administration.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            userRepository.save(new User("admin", "admin@school.com", 
                passwordEncoder.encode("admin123"), User.Role.ADMINISTRATOR));
            userRepository.save(new User("manager", "manager@school.com", 
                passwordEncoder.encode("manager123"), User.Role.MANAGER));
            userRepository.save(new User("teacher", "teacher@school.com", 
                passwordEncoder.encode("teacher123"), User.Role.TEACHER));
        }
    }
}