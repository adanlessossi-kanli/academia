package tg.academia.administration.entity;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldImplementUserDetailsCorrectly() {
        // Given
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRole(User.Role.ADMIN);
        user.setEnabled(true);

        // When & Then
        assertEquals("testuser", user.getUsername());
        assertEquals("password", user.getPassword());
        assertTrue(user.isEnabled());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    void shouldReturnCorrectAuthorities() {
        // Given
        User adminUser = new User();
        adminUser.setRole(User.Role.ADMIN);

        User teacherUser = new User();
        teacherUser.setRole(User.Role.TEACHER);

        User studentUser = new User();
        studentUser.setRole(User.Role.STUDENT);

        // When
        Collection<? extends GrantedAuthority> adminAuthorities = adminUser.getAuthorities();
        Collection<? extends GrantedAuthority> teacherAuthorities = teacherUser.getAuthorities();
        Collection<? extends GrantedAuthority> studentAuthorities = studentUser.getAuthorities();

        // Then
        assertEquals(1, adminAuthorities.size());
        assertEquals(1, teacherAuthorities.size());
        assertEquals(1, studentAuthorities.size());

        assertTrue(adminAuthorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(teacherAuthorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER")));
        assertTrue(studentAuthorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_STUDENT")));
    }

    @Test
    void shouldHandleDisabledUser() {
        // Given
        User user = new User();
        user.setEnabled(false);

        // When & Then
        assertFalse(user.isEnabled());
    }

    @Test
    void shouldSetAndGetAllFields() {
        // Given
        User user = new User();

        // When
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setRole(User.Role.TEACHER);
        user.setEnabled(true);

        // Then
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@test.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals(User.Role.TEACHER, user.getRole());
        assertTrue(user.isEnabled());
    }

    @Test
    void shouldTestRoleEnum() {
        // When & Then
        assertEquals(3, User.Role.values().length);
        assertEquals(User.Role.ADMIN, User.Role.valueOf("ADMIN"));
        assertEquals(User.Role.TEACHER, User.Role.valueOf("TEACHER"));
        assertEquals(User.Role.STUDENT, User.Role.valueOf("STUDENT"));
    }
}