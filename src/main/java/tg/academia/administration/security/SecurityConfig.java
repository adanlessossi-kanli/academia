package tg.academia.administration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tg.academia.administration.repository.UserRepository;
import tg.academia.administration.exception.ResourceNotFoundException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Profile("!test")
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;
    
    private static final String[] PUBLIC_ENDPOINTS = {
        "/api/auth/**", "/h2-console/**", "/graphql", 
        "/swagger-ui/**", "/api-docs/**", "/actuator/health"
    };
    
    private static final String[] TEACHER_ADMIN_ENDPOINTS = {
        "/api/students/**", "/api/grades/**", "/api/attendance/**"
    };
    
    private static final String[] ADMIN_ONLY_ENDPOINTS = {
        "/api/teachers/**", "/api/bulk/**"
    };

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        return new JwtAuthenticationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(this::configureAuthorization)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
            .build();
    }
    
    private void configureAuthorization(org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth.requestMatchers(PUBLIC_ENDPOINTS).permitAll()
            .requestMatchers(TEACHER_ADMIN_ENDPOINTS).hasAnyRole("ADMIN", "TEACHER")
            .requestMatchers(ADMIN_ONLY_ENDPOINTS).hasRole("ADMIN")
            .anyRequest().authenticated();
    }
}