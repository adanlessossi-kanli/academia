package tg.academia.administration.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI academiaOpenAPI() {
        return new OpenAPI()
                .info(createApiInfo())
                .addSecurityItem(createSecurityRequirement())
                .components(createComponents());
    }
    
    private Info createApiInfo() {
        return new Info()
                .title("Academia School Administration API")
                .description("""
                    Comprehensive REST API for school administration.
                    Supports student management, teacher administration, class organization,
                    grading, and attendance tracking for grades 1-6.
                    """)
                .version("1.0.0")
                .contact(new io.swagger.v3.oas.models.info.Contact()
                        .name("Academia Support")
                        .email("support@academia.tg"));
    }
    
    private io.swagger.v3.oas.models.security.SecurityRequirement createSecurityRequirement() {
        return new io.swagger.v3.oas.models.security.SecurityRequirement()
                .addList("Bearer Authentication");
    }
    
    private io.swagger.v3.oas.models.Components createComponents() {
        return new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes("Bearer Authentication", 
                    new io.swagger.v3.oas.models.security.SecurityScheme()
                        .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("JWT token for API authentication"));
    }
}
