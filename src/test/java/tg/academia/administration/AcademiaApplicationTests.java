package tg.academia.administration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import tg.academia.administration.config.TestSecurityConfig;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class AcademiaApplicationTests {

    @Test
    void contextLoads() {
    }

}
