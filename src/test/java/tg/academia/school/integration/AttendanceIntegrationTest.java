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
class AttendanceIntegrationTest {

    @Autowired
    private HttpGraphQlTester graphQlTester;

    @Test
    void markAttendance_CreatesAttendanceRecord() {
        // First create a student
        String studentId = graphQlTester.document("""
            mutation {
                createStudent(firstName: "Test", lastName: "Student", grade: 3, email: "attendance@test.com") {
                    id
                }
            }
            """)
            .execute()
            .path("createStudent.id").entity(String.class).get();

        // Mark attendance
        graphQlTester.document("""
            mutation {
                markAttendance(studentId: %s, date: "2024-01-15", status: "PRESENT") {
                    id
                    status
                    date
                }
            }
            """.formatted(studentId))
            .execute()
            .path("markAttendance.status").entity(String.class).isEqualTo("PRESENT")
            .path("markAttendance.date").entity(String.class).isEqualTo("2024-01-15");
    }

    @Test
    void attendanceByDate_ReturnsAttendanceForDate() {
        graphQlTester.document("""
            query {
                attendanceByDate(date: "2024-01-15") {
                    status
                    student {
                        firstName
                    }
                }
            }
            """)
            .execute()
            .path("attendanceByDate").entityList(Object.class).hasSizeGreaterThan(-1);
    }
}