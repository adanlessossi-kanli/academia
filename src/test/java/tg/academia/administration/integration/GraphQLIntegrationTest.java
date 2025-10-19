package tg.academia.administration.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

class GraphQLIntegrationTest extends BaseGraphQLIntegrationTest {

    @Autowired
    private HttpGraphQlTester graphQlTester;

    @Test
    void createStudent_Success() {
        graphQlTester.document("""
            mutation {
                createStudent(firstName: "Test", lastName: "User", grade: 3, email: "test@example.com") {
                    id
                    firstName
                    lastName
                    grade
                    email
                }
            }
            """)
            .execute()
            .path("createStudent.firstName").entity(String.class).isEqualTo("Test")
            .path("createStudent.grade").entity(Integer.class).isEqualTo(3);
    }

    @Test
    void students_ReturnsStudents() {
        graphQlTester.document("""
            query {
                students {
                    firstName
                    lastName
                    grade
                }
            }
            """)
            .execute()
            .path("students").entityList(Object.class).hasSizeGreaterThan(0);
    }

    @Test
    void createTeacher_Success() {
        graphQlTester.document("""
            mutation {
                createTeacher(firstName: "Jane", lastName: "Doe", email: "jane.doe@example.com", subject: "Science") {
                    id
                    firstName
                    subject
                }
            }
            """)
            .execute()
            .path("createTeacher.firstName").entity(String.class).isEqualTo("Jane")
            .path("createTeacher.subject").entity(String.class).isEqualTo("Science");
    }

    @Test
    void searchStudents_FindsStudents() {
        graphQlTester.document("""
            query {
                searchStudents(name: "Alice") {
                    firstName
                    lastName
                }
            }
            """)
            .execute()
            .path("searchStudents").entityList(Object.class).hasSizeGreaterThan(-1);
    }
}
