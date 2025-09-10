package tg.academia.administration.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureHttpGraphQlTester
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class PaginationIntegrationTest {

    @Autowired
    private HttpGraphQlTester graphQlTester;

    @Test
    void students_WithLimit_ReturnsLimitedResults() {
        graphQlTester.document("""
            query {
                students(limit: 1) {
                    firstName
                    lastName
                }
            }
            """)
            .execute()
            .path("students").entityList(Object.class).hasSize(1);
    }

    @Test
    void students_WithoutLimit_ReturnsAllResults() {
        graphQlTester.document("""
            query {
                students {
                    firstName
                    lastName
                }
            }
            """)
            .execute()
            .path("students").entityList(Object.class).hasSizeGreaterThan(1);
    }
}
