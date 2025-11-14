package org.example.service;

import org.example.dto.StatsResponse;
import org.example.repository.DnaRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatsService {
    private final DnaRecordRepository repository;

    @Autowired
    public StatsService(DnaRecordRepository repository) {
        this.repository = repository;
    }

    public StatsResponse getStats() {

        // 1. Usamos el método del Repositorio
        long mutantCount = repository.countByIsMutant(true);
        long humanCount = repository.countByIsMutant(false);


        // 2. Calculamos el ratio, manejando división por cero [cite: 30, 69]
        double ratio;
        if (humanCount == 0) {
            // Si no hay humanos, el ratio puede ser 0 o el total de mutantes
            // (depende de la definición, 0.0 es más seguro)
            ratio = mutantCount > 0 ? (double) mutantCount : 0.0;
        } else {
            ratio = (double) mutantCount / humanCount;
        }

        // 3. Devolvemos el DTO
        return new StatsResponse(mutantCount, humanCount, ratio);
    }
}
