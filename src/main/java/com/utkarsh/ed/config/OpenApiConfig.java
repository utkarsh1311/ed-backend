package com.utkarsh.ed.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("EdTech Scheduling Platform API")
                        .description(
                                """
                                REST API for an EdTech platform that connects **Teachers (India / IST)** \
                                with **Students (Australia / AEDT or AEST)**.

                                Core resources:
                                - **Students** — Australian student profiles with parent contact details
                                - **Teachers** — Indian teacher profiles
                                - **Subjects** — Configurable subject catalogue
                                - **Class Schedules** — Recurring weekly rules (teacher × student × subject × time slot)
                                - **Class Sessions** — Individual session occurrences auto-generated from schedules

                                All timestamps are stored in **IST (UTC+5:30)**. \
                                The server timezone is forced to `Asia/Kolkata`.
                                """)
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Utkarsh Maurya")
                                .email("utkarsh@ed.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local development")));
    }
}
