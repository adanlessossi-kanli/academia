package tg.academia.administration.health;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Status;
import tg.academia.administration.repository.StudentRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseHealthIndicatorTest {
    
    @Mock
    private StudentRepository studentRepository;
    
    @InjectMocks
    private DatabaseHealthIndicator healthIndicator;
    
    @Test
    void health_WhenDatabaseAvailable_ReturnsUp() {
        when(studentRepository.count()).thenReturn(10L);
        
        var health = healthIndicator.health();
        
        assertEquals(Status.UP, health.getStatus());
        assertEquals(10L, health.getDetails().get("studentCount"));
    }
    
    @Test
    void health_WhenDatabaseUnavailable_ReturnsDown() {
        when(studentRepository.count()).thenThrow(new RuntimeException("DB error"));
        
        var health = healthIndicator.health();
        
        assertEquals(Status.DOWN, health.getStatus());
        assertTrue(health.getDetails().containsKey("error"));
    }
}
