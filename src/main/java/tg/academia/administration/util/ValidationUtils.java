package tg.academia.administration.util;

import tg.academia.administration.exception.ValidationException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Utility class for common validation operations using modern Java features.
 */
public final class ValidationUtils {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s'-]{2,50}$");
    
    private ValidationUtils() {
        // Utility class
    }
    
    public static void validateGrade(Integer grade) {
        if (grade == null || grade < 1 || grade > 6) {
            throw new ValidationException("Invalid grade", 
                Map.of("grade", "Grade must be between 1 and 6"));
        }
    }
    
    public static void validateEmail(String email) {
        if (email == null || email.isBlank() || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Invalid email", 
                Map.of("email", "Email must be valid"));
        }
    }
    
    public static void validateName(String name, String fieldName) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Invalid " + fieldName, 
                Map.of(fieldName, fieldName + " is required"));
        }
        
        if (!NAME_PATTERN.matcher(name.trim()).matches()) {
            throw new ValidationException("Invalid " + fieldName, 
                Map.of(fieldName, fieldName + " must contain only letters, spaces, hyphens, and apostrophes"));
        }
    }
    
    public static <T> ValidationBuilder<T> validate(T value, String fieldName) {
        return new ValidationBuilder<>(value, fieldName);
    }
    
    public static final class ValidationBuilder<T> {
        private final T value;
        private final String fieldName;
        private final Map<String, String> errors = new HashMap<>();
        
        private ValidationBuilder(T value, String fieldName) {
            this.value = value;
            this.fieldName = fieldName;
        }
        
        public ValidationBuilder<T> isNotNull() {
            if (value == null) {
                errors.put(fieldName, fieldName + " is required");
            }
            return this;
        }
        
        public ValidationBuilder<T> matches(Predicate<T> predicate, String errorMessage) {
            if (value != null && !predicate.test(value)) {
                errors.put(fieldName, errorMessage);
            }
            return this;
        }
        
        public ValidationBuilder<T> isNotBlank() {
            if (value instanceof String str && (str == null || str.isBlank())) {
                errors.put(fieldName, fieldName + " cannot be blank");
            }
            return this;
        }
        
        public void throwIfInvalid() {
            if (!errors.isEmpty()) {
                throw new ValidationException("Validation failed", errors);
            }
        }
        
        public T get() {
            throwIfInvalid();
            return value;
        }
    }
}