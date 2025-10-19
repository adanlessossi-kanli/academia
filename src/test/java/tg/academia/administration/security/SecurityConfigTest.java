package tg.academia.administration.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@org.springframework.context.annotation.Import(tg.academia.administration.config.TestSecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void passwordEncoder_ShouldEncodePasswords() {
        String rawPassword = "test123";
        String encoded = passwordEncoder.encode(rawPassword);
        
        assertTrue(passwordEncoder.matches(rawPassword, encoded));
    }
}