package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // Requerido por JPA
@Entity // Le dice a Spring que esta clase es una tabla
@Table(name = "dna_records") // Nombre de la tabla en la BD
public class DnaRecord {

    @Id // Marca esta variable como la Clave Primaria
    @Column(name = "dna_hash", unique = true, nullable = false)
    private String dnaHash; // El ID único (nuestro "hash")

    @Column(name = "is_mutant", nullable = false)
    private boolean isMutant;

    // Constructor que usaremos
    public DnaRecord(String dnaHash, boolean isMutant) {
        this.dnaHash = dnaHash;
        this.isMutant = isMutant;
    }
}
