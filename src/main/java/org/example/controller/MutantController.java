package org.example.controller;

import org.example.dto.DnaRequest;
import org.example.dto.StatsResponse;
import org.example.service.MutantDetector;
import org.example.service.MutantService;
import org.example.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping // Lo dejamos en la raíz
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
    public ResponseEntity<StatsResponse> getStats() {
        StatsResponse stats = statsService.getStats();
        return ResponseEntity.ok(stats);
    }
}
