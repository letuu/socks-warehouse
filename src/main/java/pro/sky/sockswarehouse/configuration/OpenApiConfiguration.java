package pro.sky.sockswarehouse.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI socksWarehouseApi() {
        return new OpenAPI()
                .info(new Info().title("Socks Warehouse API").description("API to manage Socks Warehouse")
                        .version("1.0.0"))
                .addTagsItem(new Tag().name("socks").description("Socks management API"));
    }
}
