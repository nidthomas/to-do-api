package com.chilborne.todoapi.web.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {


  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
      .addSecurityItem(new SecurityRequirement()
        .addList("bearerAuth"))
      .components(new Components()
        .addSecuritySchemes("bearerAuth", new SecurityScheme()
          .name("Bearer Token")
          .type(SecurityScheme.Type.HTTP)
          .scheme("bearer")
          .bearerFormat("JWT")
        )
      )
      .info(new Info()
        .title("to-dp-api")
        .description("Proyecto Final Spring de Tokio School")
        .contact(new Contact()
          .name("Christopher Hilborne")
          .email("chris.hilborne@gmail.com")
          .url("https://github.com/ChrisHilborne"))
        .version("1.0")
      );

  }

}
