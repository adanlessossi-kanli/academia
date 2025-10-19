package tg.academia.administration.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "mySecretKeyForTestingThatShouldBeAtLeast256BitsLong");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L);
        
        userDetails = User.builder()
                .username("testuser")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
    }

    @Test
    void shouldGenerateToken() {
        String token = jwtUtil.generateToken(userDetails);
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void shouldExtractUsername() {
        String token = jwtUtil.generateToken(userDetails);
        String username = jwtUtil.extractUsername(token);
        
        assertEquals("testuser", username);
    }

    @Test
    void shouldValidateToken() {
        String token = jwtUtil.generateToken(userDetails);
        
        assertTrue(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    void shouldRejectInvalidToken() {
        String invalidToken = "invalid.token.here";
        
        assertThrows(Exception.class, () -> jwtUtil.validateToken(invalidToken, userDetails));
    }

    @Test
    void shouldExtractExpiration() {
        String token = jwtUtil.generateToken(userDetails);
        
        assertNotNull(jwtUtil.extractExpiration(token));
        assertTrue(jwtUtil.extractExpiration(token).getTime() > System.currentTimeMillis());
    }
}