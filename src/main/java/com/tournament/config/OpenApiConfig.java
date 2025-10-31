package com.tournament.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Online Tournament System API",
                description = "REST API for managing users, games, tournaments, participations, and matches.",
                version = "1.0.0",
                contact = @Contact(name = "Online Tournament Support", email = "support@tournament.local"),
                license = @License(name = "MIT License", url = "https://opensource.org/licenses/MIT")
        ),
        security = @SecurityRequirement(name = "bearerAuth"),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local environment")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}
