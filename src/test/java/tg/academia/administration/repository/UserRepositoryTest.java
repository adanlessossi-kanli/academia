package tg.academia.administration.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tg.academia.administration.entity.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFindUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@school.edu");
        user.setPassword("password");
        user.setRole(User.Role.ADMIN);

        User saved = userRepository.save(user);
        assertNotNull(saved.getId());

        Optional<User> found = userRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("testuser", found.get().getUsername());
    }

    @Test
    void shouldFindByUsername() {
        User user = new User();
        user.setUsername("admin");
        user.setEmail("admin@school.edu");
        user.setPassword("password");
        user.setRole(User.Role.ADMIN);
        userRepository.save(user);

        Optional<User> found = userRepository.findByUsername("admin");
        assertTrue(found.isPresent());
        assertEquals("admin", found.get().getUsername());
        assertEquals(User.Role.ADMIN, found.get().getRole());
    }

    @Test
    void shouldReturnEmptyWhenUsernameNotFound() {
        Optional<User> found = userRepository.findByUsername("nonexistent");
        assertFalse(found.isPresent());
    }

    @Test
    void shouldFindByEmail() {
        User user = new User();
        user.setUsername("teacher");
        user.setPassword("password");
        user.setEmail("teacher@school.edu");
        user.setRole(User.Role.TEACHER);
        userRepository.save(user);

        Optional<User> found = userRepository.findByEmail("teacher@school.edu");
        assertTrue(found.isPresent());
        assertEquals("teacher", found.get().getUsername());
    }
}