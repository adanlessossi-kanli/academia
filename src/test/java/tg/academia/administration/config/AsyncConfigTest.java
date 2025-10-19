package tg.academia.administration.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@org.springframework.context.annotation.Import(tg.academia.administration.config.TestSecurityConfig.class)
class AsyncConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void shouldCreateTaskExecutorBean() {
        assertTrue(applicationContext.containsBean("taskExecutor"));
        Object taskExecutor = applicationContext.getBean("taskExecutor");
        assertInstanceOf(ThreadPoolTaskExecutor.class, taskExecutor);
    }
}