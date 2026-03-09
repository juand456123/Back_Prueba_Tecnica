package com.btg.btg_funds.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI btgFundsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BTG Funds API")
                        .description("API para gestión de fondos, suscripciones, cancelaciones y transacciones")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Juan Diego Guzmán Herrera")
                                .url("https://github.com/juand456123/Back_Prueba_Tecnica"))
                        .license(new License()
                                .name("Uso académico / prueba técnica")))
                .externalDocs(new ExternalDocumentation()
                        .description("Repositorio del proyecto")
                        .url("https://github.com/juand456123/Back_Prueba_Tecnica"));
    }
}
