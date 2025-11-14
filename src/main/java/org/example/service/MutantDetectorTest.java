package org.example.service;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;

class MutantDetectorTest {
    private MutantDetector detector;

    @BeforeEach
    void setUp() {
        // Creamos una nueva instancia antes de cada test
        detector = new MutantDetector();
    }

    @Test
    void testIsMutant_CasoMutanteDelPDF() {
        // Este ADN tiene "CCCCTA" horizontal y "A" en diagonal
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void testIsMutant_CasoHumano_CeroSecuencias() {
        String[] dna = {"ATGC", "GCTA", "TCGA", "CGAT"};
        assertFalse(detector.isMutant(dna));
    }

    @Test
    void testIsMutant_CasoHumano_UNA_SolaSecuencia() {
        // REGLA CLAVE: "más de una secuencia"
        // Este ADN solo tiene "AAAA", no es mutante.
        String[] dna = {"AAAA", "CTCT", "GCGC", "TATA"};
        assertFalse(detector.isMutant(dna));
    }

    @Test
    void testIsMutant_CasoMutante_VerticalYHorizontal() {
        String[] dna = {
                "GTCG",
                "GAGC",
                "GACG",
                "GGGG" // Secuencia vertical (G) y horizontal (G)
        };
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void testIsMutant_CasoMutante_DiagonalesOpuestas() {
        String[] dna = {
                "ACGT",
                "CACG",
                "GCAC",
                "TACA" // "A" en diagonal \ y "C" en diagonal /
        };
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void testIsMutant_Error_NoEsCuadrada() {
        String[] dna = {"ATGC", "AT"};
        // Verificamos que lance la excepción que definiste
        assertThrows(IllegalArgumentException.class, () -> {
            detector.isMutant(dna);
        });
    }

    @Test
    void testIsMutant_Error_CaracterInvalido() {
        String[] dna = {"ATGC", "GCTA", "TCGA", "CGZX"}; // Contiene 'X' y 'Z'
        assertThrows(IllegalArgumentException.class, () -> {
            detector.isMutant(dna);
        });
    }

    @Test
    void testIsMutant_Error_NullInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            detector.isMutant(null);
        });
    }

    @Test
    void testIsMutant_Error_EmptyInput() {
        String[] dna = {};
        assertThrows(IllegalArgumentException.class, () -> {
            detector.isMutant(dna);
        });
    }

}
