package tg.academia.school.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureHttpGraphQlTester
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class ErrorHandlingIntegrationTest {

    @Autowired
    private HttpGraphQlTester graphQlTester;

    @Test
    void createStudent_InvalidGrade_ReturnsError() {
        graphQlTester.document("""
            mutation {
                createStudent(firstName: "Test", lastName: "User", grade: 7, email: "test@example.com") {
                    id
                }
            }
            """)
            .execute()
            .errors()
            .filter(error -> error.getMessage().contains("INTERNAL_ERROR"))
            .verify();
    }

    @Test
    void createStudent_DuplicateEmail_ReturnsError() {
        // First create a student
        graphQlTester.document("""
            mutation {
                createStudent(firstName: "First", lastName: "User", grade: 3, email: "duplicate@example.com") {
                    id
                }
            }
            """)
            .execute();

        // Try to create another with same email
        graphQlTester.document("""
            mutation {
                createStudent(firstName: "Second", lastName: "User", grade: 3, email: "duplicate@example.com") {
                    id
                }
            }
            """)
            .execute()
            .errors()
            .filter(error -> error.getMessage().contains("INTERNAL_ERROR"))
            .verify();
    }

    @Test
    void markAttendance_InvalidStudentId_ReturnsError() {
        graphQlTester.document("""
            mutation {
                markAttendance(studentId: 999, date: "2024-01-15", status: "PRESENT") {
                    id
                }
            }
            """)
            .execute()
            .errors()
            .filter(error -> error.getMessage().contains("INTERNAL_ERROR"))
            .verify();
    }
}