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
                .info(new Info()
                        .title("Academia School Administration API")
                        .description("REST API for managing school administration (grades 1-6)")
                        .version("1.0.0"));
    }
}
