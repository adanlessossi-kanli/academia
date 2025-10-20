package tg.academia.administration.config;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class CorsConfigTest {
    
    @Test
    void corsFilter_CreatesFilterWithConfiguration() {
        var config = new CorsConfig();
        ReflectionTestUtils.setField(config, "allowedOrigins", new String[]{"http://localhost:3000"});
        
        var filter = config.corsFilter();
        
        assertNotNull(filter);
    }
}
