package tg.academia.administration.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tg.academia.administration.entity.User;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername_ShouldReturnUser() {
        User user = new User("testuser", "test@example.com", "password", User.Role.TEACHER);
        userRepository.save(user);

        assertTrue(userRepository.findByUsername("testuser").isPresent());
        assertFalse(userRepository.findByUsername("nonexistent").isPresent());
    }

    @Test
    void findByEmail_ShouldReturnUser() {
        User user = new User("testuser", "test@example.com", "password", User.Role.MANAGER);
        userRepository.save(user);

        assertTrue(userRepository.findByEmail("test@example.com").isPresent());
        assertFalse(userRepository.findByEmail("nonexistent@example.com").isPresent());
    }
}