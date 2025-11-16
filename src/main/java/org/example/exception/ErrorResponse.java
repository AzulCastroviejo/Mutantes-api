package org.example.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Respuesta de error estándar")
public class ErrorResponse {

    @Schema(description = "Timestamp del error", example = "2025-01-15T10:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Código de estado HTTP", example = "400")
    private int status;

    @Schema(description = "Mensaje de error", example = "Invalid DNA sequence")
    private String message;

    @Schema(description = "Ruta del endpoint", example = "/mutant")
    private String path;
}
