package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.DnaRequest;
import org.example.dto.StatsResponse;
import org.example.service.MutantDetector;
import org.example.service.MutantService;
import org.example.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping
@Tag(name = "Mutant Detector", description = "API para detección de mutantes mediante análisis de ADN")
public class MutantController {
    // 1. Inyectamos tu servicio
    private final MutantService mutantService;
    private final StatsService statsService;

    @Autowired
    public MutantController(MutantService mutantService, StatsService statsService) {
        this.mutantService = mutantService;
        this.statsService = statsService;
    }

    // 2. Definimos el endpoint POST /mutant
    @PostMapping("/mutant")
    @Operation(
            summary = "Verificar si un ADN es mutante",
            description = """
            Analiza una secuencia de ADN para determinar si pertenece a un mutante.
            
            Un humano es mutante si se encuentran más de una secuencia de cuatro letras 
            iguales en dirección horizontal, vertical o diagonal.
            
            El resultado se almacena en caché para evitar análisis duplicados.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Es mutante - Se detectaron más de una secuencia de 4 letras iguales",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "No es mutante - Se detectó una o ninguna secuencia",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request inválido - El ADN no cumple con el formato requerido (matriz NxN, solo A/T/C/G)",
                    content = @Content
            )
    })
    public ResponseEntity<Void> checkMutant(@RequestBody DnaRequest dnaRequest) {

        try {
            boolean isMutant = mutantService.processDna(dnaRequest.getDna());

            if (isMutant) {
                // Si es mutante, devolvemos 200 OK
                return ResponseEntity.ok().build(); // [cite: 26, 54]
            } else {
                // Si no es mutante, devolvemos 403 Forbidden
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // [cite: 26, 55]
            }

        } catch (IllegalArgumentException e) {
            // Si tu detector lanza la excepción (ADN inválido),
            // también es 'Forbidden'
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    @GetMapping("/stats")
    @Operation(
            summary = "Obtener estadísticas de verificaciones",
            description = """
            Retorna estadísticas de todas las verificaciones de ADN realizadas:
            - Cantidad de ADN mutante detectado
            - Cantidad de ADN humano detectado  
            - Ratio: mutantes / humanos
            """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estadísticas obtenidas exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StatsResponse.class)
                    )
            )
    })
    public ResponseEntity<StatsResponse> getStats() {
        StatsResponse stats = statsService.getStats();
        return ResponseEntity.ok(stats);
    }
}
