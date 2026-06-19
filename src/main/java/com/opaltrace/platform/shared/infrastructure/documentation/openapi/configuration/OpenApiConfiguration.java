package com.opaltrace.platform.shared.infrastructure.documentation.openapi.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {
    @Value("${spring.application.name}")
    String applicationName;

    @Value("${documentation.application.description}")
    String applicationDescription;

    @Value("${documentation.application.version}")
    String applicationVersion;
    @Bean
    public OpenAPI opalTracePlatformOpenApi() {

        var openApi = new OpenAPI();
        openApi
                .info(new Info()
                        .title(this.applicationName)
                        .description(this.applicationDescription)
                        .version(this.applicationVersion)
                        .contact(new Contact()
                                .name("OpalTrace Support")
                                .email("support@opaltrace.com")
                                .url("https://opaltrace.com/support"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .externalDocs(new ExternalDocumentation()
                        .description("OpalTrace Platform Documentation")
                        .url("https://opaltrace.com/docs"));

        openApi.servers(List.of(
                new Server()
                        .url("https://opaltrace-platform-production.up.railway.app")
                        .description("Production Environment"),
                new Server()
                        .url("http://localhost:8080")
                        .description("Local Development Environment")
        ));

        // Add a security scheme
       /*final String securitySchemeName = "bearerAuth";

       openApi.addSecurityItem(new SecurityRequirement()
                       .addList(securitySchemeName))
               .components(new Components()
                       .addSecuritySchemes(securitySchemeName,
                               new SecurityScheme()
                                       .name(securitySchemeName)
                                       .type(SecurityScheme.Type.HTTP)
                                       .scheme("bearer")
                                       .bearerFormat("JWT")
                                       .description("JWT Bearer token for API authentication")));*/

        return openApi;
    }
}