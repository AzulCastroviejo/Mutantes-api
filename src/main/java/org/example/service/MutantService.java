package org.example.service;

import jakarta.transaction.Transactional;
import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class MutantService {
    private final MutantDetector mutantDetector;
    private final DnaRecordRepository repository;

    @Autowired
    public MutantService(MutantDetector mutantDetector, DnaRecordRepository repository) {
        this.mutantDetector = mutantDetector;
        this.repository = repository;
    }

    @Transactional // Asegura que la operación (revisar y guardar) sea atómica
    public boolean processDna(String[] dna) {

        // 1. Crear un ID único para este ADN
        String hash = createDnaHash(dna);

       // 2. Revisar si ya analizamos este ADN [cite: 29, 63, 156]
        Optional<DnaRecord> existingRecord = repository.findById(hash);

        if (existingRecord.isPresent()) {
            // Si ya existe, devolvemos el resultado guardado
            return existingRecord.get().isMutant();
        }

        // 3. Si es nuevo, lo analizamos
        boolean isMutant = mutantDetector.isMutant(dna);

        // 4. Guardamos el resultado en la base de datos
        DnaRecord newRecord = new DnaRecord(hash, isMutant);
        repository.save(newRecord);

        // 5. Devolvemos el resultado
        return isMutant;
    }

    /**
     * Crea un hash SHA-256 del DNA para usarlo como ID único
     * Esto evita duplicados y permite búsquedas rápidas O(1)
     */
    private String createDnaHash(String[] dna) {
        try {
            // Unir todas las filas del DNA en un solo string
            String dnaString = String.join("", dna);

            // Crear instancia de SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Calcular hash
            byte[] hashBytes = digest.digest(dnaString.getBytes(StandardCharsets.UTF_8));

            // Convertir bytes a hexadecimal (64 caracteres)
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            // SHA-256 siempre está disponible, pero por si acaso
            throw new RuntimeException("Error al calcular hash del DNA", e);
        }
    }
}
