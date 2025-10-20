package tg.academia.administration.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintWriter;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RateLimitFilterTest {
    
    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private FilterChain filterChain;
    
    @Mock
    private PrintWriter writer;
    
    @InjectMocks
    private RateLimitFilter rateLimitFilter;
    
    @Test
    void doFilterInternal_WithinLimit_AllowsRequest() throws Exception {
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        
        rateLimitFilter.doFilterInternal(request, response, filterChain);
        
        verify(filterChain).doFilter(request, response);
        verify(response, never()).setStatus(429);
    }
    
    @Test
    void doFilterInternal_ExceedsLimit_ReturnsError() throws Exception {
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(response.getWriter()).thenReturn(writer);
        
        for (int i = 0; i < 101; i++) {
            rateLimitFilter.doFilterInternal(request, response, filterChain);
        }
        
        verify(response, atLeastOnce()).setStatus(429);
    }
}
