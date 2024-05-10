package com.example.idata.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
@Configuration
public class SwaggerConfiguration {

    @Value("${server.port}")
    private String port;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("accessToken",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("iData API")
                        .version("1.0.0")
                        .description("Why you should use this API?"))
                .servers(new ArrayList<>(){{
                    add(new Server().url("http://localhost:"+port).description("Local ENV"));
                    add(new Server().url("https://store.automatex.dev").description("Dev ENV"));
                }});
    }
}