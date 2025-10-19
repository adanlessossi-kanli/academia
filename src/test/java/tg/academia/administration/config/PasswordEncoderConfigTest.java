package tg.academia.administration.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@org.springframework.context.annotation.Import(tg.academia.administration.config.TestSecurityConfig.class)
class PasswordEncoderConfigTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldEncodePassword() {
        String rawPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        
        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    void shouldNotMatchIncorrectPassword() {
        String rawPassword = "testPassword";
        String wrongPassword = "wrongPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        
        assertFalse(passwordEncoder.matches(wrongPassword, encodedPassword));
    }
}