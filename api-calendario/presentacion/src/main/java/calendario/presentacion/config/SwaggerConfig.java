package calendario.presentacion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI configurarOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("API Calendario Laboral")
                .description("Taller 1 - Arquitectura de Software II - ITM")
                .version("1.0.0"));
    }
}
