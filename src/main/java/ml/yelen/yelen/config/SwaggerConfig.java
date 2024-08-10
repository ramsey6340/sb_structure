package ml.yelen.yelen.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
public class SwaggerConfig {
    @Bean
    public OpenAPI productOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Yelen Service")
                        .description("Service de gestion de Yelen").version("1.0.0"));
    }
}
