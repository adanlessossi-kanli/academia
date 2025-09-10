package tg.academia.administration.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OpenApiConfigTest {

    @Test
    void academiaOpenAPI_ReturnsConfiguredOpenAPI() {
        OpenApiConfig config = new OpenApiConfig();
        
        OpenAPI openAPI = config.academiaOpenAPI();
        
        assertThat(openAPI.getInfo().getTitle()).isEqualTo("Academia School Administration API");
        assertThat(openAPI.getInfo().getVersion()).isEqualTo("1.0.0");
        assertThat(openAPI.getInfo().getDescription()).contains("school administration");
    }
}
