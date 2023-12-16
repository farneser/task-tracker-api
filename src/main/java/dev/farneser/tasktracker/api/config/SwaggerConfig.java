package dev.farneser.tasktracker.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Farneser",
                        email = "farneser1@gmail.com",
                        url = "https://farneser.t.me"
                ),
                description = "Documentation for task tracker web api module",
                title = "Task tracker specification",
                version = "1.0"
        ),
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
@Configuration
@RequiredArgsConstructor
@ConditionalOnExpression(value = "${springdoc.swagger-ui.enabled:true}")
public class SwaggerConfig {

    @Value("${server.servlet.scheme:http}")
    private String serverScheme;

    @Value("${server.servlet.host:localhost}")
    private String serverHost;

    @Value("${server.servlet.port:8080}")
    private String serverPort;

    @Value("${server.servlet.context-path:/}")
    private String contextPath;

    @Value("${application.swagger.enable-ssl:false}")
    private boolean enableSsl;

    @Value("${application.swagger.hosts}")
    private String[] hosts;

    private static String getDefaultServer(String serverScheme, String serverHost, String serverPort, String contextPath) {
        return serverScheme + "://" + serverHost + ":" + serverPort + contextPath;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        var apiDocs = new OpenAPI();

        var defaultServerUrl = getDefaultServer(serverScheme, serverHost, serverPort, contextPath);


        if (enableSsl) {
            var sslServerUrl = getDefaultServer("https", serverHost, serverPort, contextPath);

            if (!sslServerUrl.equals(defaultServerUrl)) {
                apiDocs.addServersItem(new Server().url(sslServerUrl));
            }
        }

        apiDocs.addServersItem(new Server().url(defaultServerUrl));

        for (String host : hosts) {
            apiDocs.addServersItem(new Server().url(host));
        }

        return apiDocs;
    }
}
