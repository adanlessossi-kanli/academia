package tg.academia.administration.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import tg.academia.administration.security.JwtUtil;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
@EnableMethodSecurity(prePostEnabled = true)
@Profile("test")
public class TestSecurityConfig {

    @Bean
    @Primary
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/h2-console/**", "/graphql/**", "/graphiql/**").permitAll()
                .anyRequest().authenticated())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS))
            .build();
    }

    @Bean
    @Primary
    public AuthenticationManager testAuthenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(testUserDetailsService());
        provider.setPasswordEncoder(testPasswordEncoder());
        return new ProviderManager(provider);
    }

    @Bean
    @Primary
    public UserDetailsService testUserDetailsService() {
        return new InMemoryUserDetailsManager(
            User.withUsername("admin")
                .password(testPasswordEncoder().encode("password"))
                .roles("ADMIN")
                .build(),
            User.withUsername("teacher")
                .password(testPasswordEncoder().encode("password"))
                .roles("TEACHER")
                .build()
        );
    }

    @Bean(name = "testPasswordEncoder")
    @Primary
    public PasswordEncoder testPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    public JwtUtil testJwtUtil() {
        JwtUtil jwtUtil = mock(JwtUtil.class);
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("test-jwt-token");
        when(jwtUtil.extractUsername(any(String.class))).thenReturn("admin");
        when(jwtUtil.validateToken(any(String.class), any(UserDetails.class))).thenReturn(true);
        return jwtUtil;
    }
}