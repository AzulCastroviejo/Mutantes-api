package org.example.service;
import org.example.service.MutantDetector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MutantDetectorTest {

    private MutantDetector mutantDetector;

    @BeforeEach
    void setUp() {
        mutantDetector = new MutantDetector();
    }

    // ========== TESTS DE MUTANTES (deben retornar true) ==========

    @Test
    @DisplayName("Debe detectar mutante con secuencias horizontal y diagonal")
    void testMutantWithHorizontalAndDiagonalSequences() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA", // Horizontal: CCCC
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante con secuencias verticales")
    void testMutantWithVerticalSequences() {
        String[] dna = {
                "ATGCGA",
                "ATGTGC",
                "ATATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante con múltiples secuencias horizontales")
    void testMutantWithMultipleHorizontalSequences() {
        String[] dna = {
                "TTTTGA", // Horizontal: TTTT
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA", // Horizontal: CCCC
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante con diagonales ascendentes y descendentes")
    void testMutantWithBothDiagonals() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante en matriz 4x4")
    void testMutantSmallDna4x4() {
        String[] dna = {
                "AAAA", // Horizontal: AAAA
                "CCCC", // Horizontal: CCCC
                "TTAT",
                "AGAC"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante en matriz grande 10x10")
    void testMutantLargeDna() {
        String[] dna = {
                "ATGCGAATGC",
                "CAGTGCCAGT",
                "TTATGTTTAT",
                "AGAAGGATAA",
                "CCCCTACCCC", // 2 horizontales
                "TCACTGTCAC",
                "ATGCGAATGC",
                "CAGTGCCAGT",
                "TTATGTTTAT",
                "AGAAGGATAA"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante con todas las bases iguales")
    void testMutantAllSameCharacter() {
        String[] dna = {
                "AAAAAA",
                "AAAAAA",
                "AAAAAA",
                "AAAAAA",
                "AAAAAA",
                "AAAAAA"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    // ========== TESTS DE HUMANOS (deben retornar false) ==========

    @Test
    @DisplayName("No debe detectar mutante con solo una secuencia")
    void testNotMutantWithOnlyOneSequence() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATTT", // Solo 1 secuencia
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("No debe detectar mutante sin secuencias")
    void testNotMutantWithNoSequences() {
        String[] dna = {
                "ATGC",
                "CAGT",
                "TTAT",
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    // ========== TESTS DE VALIDACIÓN (deben lanzar excepción o retornar false) ==========

    @Test
    @DisplayName("Debe rechazar ADN nulo")
    void testNullDna() {
        assertThrows(IllegalArgumentException.class, () -> {
            mutantDetector.isMutant(null);
        });
    }

    @Test
    @DisplayName("Debe rechazar ADN vacío")
    void testEmptyDna() {
        String[] dna = {};
        assertThrows(IllegalArgumentException.class, () -> {
            mutantDetector.isMutant(dna);
        });
    }

    @Test
    @DisplayName("Debe rechazar matriz no cuadrada")
    void testNonSquareMatrix() {
        String[] dna = {
                "ATGCGA", // 6 caracteres
                "CAGTGC", // 6 caracteres
                "TTATGT"  // Solo 3 filas (3x6, no es cuadrada)
        };
        assertThrows(IllegalArgumentException.class, () -> {
            mutantDetector.isMutant(dna);
        });
    }

    @Test
    @DisplayName("Debe rechazar caracteres inválidos")
    void testInvalidCharacters() {
        String[] dna = {
                "ATGCGA",
                "CAGTXC", // 'X' es inválido
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertThrows(IllegalArgumentException.class, () -> {
            mutantDetector.isMutant(dna);
        });
    }

    @Test
    @DisplayName("Debe rechazar fila nula")
    void testNullRowInArray() {
        String[] dna = {
                "ATGCGA",
                null, // Fila nula
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertThrows(IllegalArgumentException.class, () -> {
            mutantDetector.isMutant(dna);
        });
    }

    // ========== TESTS DE RENDIMIENTO ==========

    @Test
    @DisplayName("Debe usar early termination para eficiencia")
    void testEarlyTermination() {
        String[] dna = {
                "AAAAGA", // Secuencia 1
                "AAAAGC", // Secuencia 2
                "TTATGT", // Ya no se revisa (early termination)
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };

        long startTime = System.nanoTime();
        boolean result = mutantDetector.isMutant(dna);
        long endTime = System.nanoTime();

        assertTrue(result);
        // Debe terminar rápido (menos de 10ms)
        assertTrue((endTime - startTime) < 10_000_000, "Algoritmo debería usar early termination");
    }

    @Test
    @DisplayName("Debe manejar matriz pequeña 4x4")
    void testSmallMatrix4x4() {
        String[] dna = {
                "ATGC",
                "CAGT",
                "TTAT",
                "AGAC"
        };
        // No tiene secuencias, debe ser humano
        assertFalse(mutantDetector.isMutant(dna));
    }
}
