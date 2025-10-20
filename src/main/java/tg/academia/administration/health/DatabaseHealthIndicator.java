package tg.academia.administration.health;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import tg.academia.administration.repository.StudentRepository;

@Component
@RequiredArgsConstructor
public class DatabaseHealthIndicator implements HealthIndicator {
    
    private final StudentRepository studentRepository;
    
    @Override
    public Health health() {
        try {
            long count = studentRepository.count();
            return Health.up()
                .withDetail("database", "available")
                .withDetail("studentCount", count)
                .build();
        } catch (Exception e) {
            return Health.down()
                .withDetail("error", e.getMessage())
                .build();
        }
    }
}
