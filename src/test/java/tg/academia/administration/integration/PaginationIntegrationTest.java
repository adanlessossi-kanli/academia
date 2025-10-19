package tg.academia.administration.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

class PaginationIntegrationTest extends BaseGraphQLIntegrationTest {

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
