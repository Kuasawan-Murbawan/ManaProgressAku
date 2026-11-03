package com.husyairi.ManaProgressAku.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {

    String securitySchemeName = "bearerAuth";
    @Bean
    public OpenAPI manaProgressAkuOpenAPI(){

        return new OpenAPI()
                .info(new Info().title("ManaProgressAku API")
                .version("1.0.0")
                .description("API documentation for ManaProgressAku, a workout progress tracking application.")
                .contact(new Contact().name("Husyairi Afiq")
                        .email("husyairiafiq@gmail.com"))
                        .license(new License().name("Personal Portfolio Project")))
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(
                                securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        ));
    }
}
