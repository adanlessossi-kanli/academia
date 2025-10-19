package tg.academia.administration.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MonitoringConfig {

    @Bean
    public Counter studentCreationCounter(MeterRegistry meterRegistry) {
        return Counter.builder("students.created")
                .description("Number of students created")
                .register(meterRegistry);
    }

    @Bean
    public Counter authenticationCounter(MeterRegistry meterRegistry) {
        return Counter.builder("authentication.attempts")
                .description("Number of authentication attempts")
                .register(meterRegistry);
    }
}