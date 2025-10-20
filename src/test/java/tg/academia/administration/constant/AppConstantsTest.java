package tg.academia.administration.constant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppConstantsTest {
    
    @Test
    void constants_HaveCorrectValues() {
        assertEquals(1, AppConstants.MIN_GRADE);
        assertEquals(6, AppConstants.MAX_GRADE);
        assertEquals("students", AppConstants.CACHE_STUDENTS);
        assertEquals("teachers", AppConstants.CACHE_TEACHERS);
        assertEquals("classes", AppConstants.CACHE_CLASSES);
        assertEquals(10, AppConstants.DEFAULT_PAGE_SIZE);
        assertEquals(100, AppConstants.MAX_PAGE_SIZE);
    }
}
