package tg.academia.administration.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

class BatchMappingIntegrationTest extends BaseGraphQLIntegrationTest {

    @Autowired
    private HttpGraphQlTester graphQlTester;

    @Test
    void students_WithSchoolClass_UsesBatchMapping() {
        graphQlTester.document("""
            query {
                students {
                    firstName
                    schoolClass {
                        name
                        teacher {
                            firstName
                        }
                    }
                }
            }
            """)
            .execute()
            .path("students").entityList(Object.class).hasSizeGreaterThan(0);
    }

    @Test
    void teachers_WithClasses_LoadsRelationships() {
        graphQlTester.document("""
            query {
                teachers {
                    firstName
                    classes {
                        name
                        students {
                            firstName
                        }
                    }
                }
            }
            """)
            .execute()
            .path("teachers").entityList(Object.class).hasSizeGreaterThan(0);
    }
}
