package com.stockmarket.stockmarketapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI openAPI() {
    Contact contact = new Contact()
    .name("Low Jie Wei")
    .url("https://www.linkedin.com/in/lowjiewei/")
    .email("lowjiewei@gmail.com");

    return new OpenAPI()
      .info(new Info()
      .title("Java Spring Boot REST API Microservice")
      .description("Java Spring Boot REST API for Stock Market Brokers")
      .contact(contact)
      .version("v1.0"));
  }

}
