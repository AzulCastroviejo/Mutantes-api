package org.example.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;
/**
 * Configuración de Swagger/OpenAPI para documentar la API REST.
 *
 * Acceso a la documentación:
 * - Swagger UI: http://localhost:8080/swagger-ui.html
 * - OpenAPI JSON: http://localhost:8080/api-docs
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Mutant Detector API",
                version = "1.0.0",
                description = """
            API REST para detectar mutantes basándose en su secuencia de ADN.
            
            Un humano es mutante si encuentra más de una secuencia de cuatro letras 
            iguales (A, T, C, G) en dirección horizontal, vertical o diagonal.
            
            Desarrollado como parte del examen técnico de MercadoLibre.
            """,
                contact = @Contact(
                        name = "Equipo de Desarrollo",
                        url = "https://github.com/AzulCastroviejo/Mutantes-api"
                )
        ),
        servers = {
                @Server(
                        description = "Servidor Local",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Servidor de Producción (Render)",
                        url = "https://mutantes-api.onrender.com"
                )
        }
)
public class SwaggerConfig {
   }