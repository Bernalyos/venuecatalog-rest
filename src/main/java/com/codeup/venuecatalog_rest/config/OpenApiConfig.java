package com.codeup.venuecatalog_rest.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Venue Catalog REST API")
                        .version("1.0")
                        .description("API REST para gestionar Eventos y Venues (almacenamiento en memoria)"));
    }
}
