package com.sedefproject.webpage.security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name="sedefbas",
                        email = "bassedef98@gmail.com",
                        url = "https://www.linkedin.com/in/sedef-ba%C5%9F-4a581b224/"
                ),
                description = "openApi documantion for my web blog :)",
                title = "OpenApi specification - sedefbas",
                version = "1.0",
                license = @License(
                        name = "license name",
                        url = "http://some-url.com"
                ),
                termsOfService = "terms of service"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url="http://localhost:8080"

                )    , @Server(
                description= "PROD ENV",
                url = "https://www.linkedin.com/in/sedef-ba%C5%9F-4a581b224/"
        )
        },
        security = {@SecurityRequirement(name = "bearerAuth")}
)
//kilit işaret
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
