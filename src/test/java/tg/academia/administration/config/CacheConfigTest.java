package tg.academia.administration.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.cache.CacheManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@org.springframework.context.annotation.Import(tg.academia.administration.config.TestSecurityConfig.class)
class CacheConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void shouldCreateCacheManagerBean() {
        assertTrue(applicationContext.containsBean("caffeineCacheManager"));
        Object cacheManager = applicationContext.getBean("caffeineCacheManager");
        assertInstanceOf(CacheManager.class, cacheManager);
    }
}