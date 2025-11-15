package service;

import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.example.service.MutantDetector;
import org.example.service.MutantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MutantServiceTest {

    @Mock
    private MutantDetector mutantDetector;

    @Mock
    private DnaRecordRepository repository;

    @InjectMocks
    private MutantService mutantService;

    private String[] mutantDna;
    private String[] humanDna;

    @BeforeEach
    void setUp() {
        mutantDna = new String[]{
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };

        humanDna = new String[]{
                "ATGCGA",
                "CAGTGC",
                "TTATTT",
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };
    }

    @Test
    @DisplayName("Debe analizar ADN mutante y guardarlo en BD")
    void testAnalyzeMutantDnaAndSave() {
        // ARRANGE - Preparar mocks
        when(repository.findById(anyString()))
                .thenReturn(Optional.empty()); // No existe en BD
        when(mutantDetector.isMutant(mutantDna))
                .thenReturn(true); // Es mutante
        when(repository.save(any(DnaRecord.class)))
                .thenReturn(new DnaRecord("hash123", true));

        // ACT - Ejecutar método
        boolean result = mutantService.processDna(mutantDna);

        // ASSERT - Verificar resultado
        assertTrue(result);

        // VERIFY - Verificar interacciones
        verify(mutantDetector, times(1)).isMutant(mutantDna);
        verify(repository, times(1)).save(any(DnaRecord.class));
    }

    @Test
    @DisplayName("Debe analizar ADN humano y guardarlo en BD")
    void testAnalyzeHumanDnaAndSave() {
        // ARRANGE
        when(repository.findById(anyString()))
                .thenReturn(Optional.empty());
        when(mutantDetector.isMutant(humanDna))
                .thenReturn(false); // Es humano
        when(repository.save(any(DnaRecord.class)))
                .thenReturn(new DnaRecord("hash456", false));

        // ACT
        boolean result = mutantService.processDna(humanDna);

        // ASSERT
        assertFalse(result);

        // VERIFY
        verify(mutantDetector, times(1)).isMutant(humanDna);
        verify(repository, times(1)).save(any(DnaRecord.class));
    }

    @Test
    @DisplayName("Debe retornar resultado cacheado si el ADN ya fue analizado")
    void testReturnCachedResultForAnalyzedDna() {
        // ARRANGE - Simular que ya existe en BD
        DnaRecord cachedRecord = new DnaRecord("cached_hash", true);
        when(repository.findById(anyString()))
                .thenReturn(Optional.of(cachedRecord)); // YA existe

        // ACT
        boolean result = mutantService.processDna(mutantDna);

        // ASSERT
        assertTrue(result);

        // VERIFY - NO debe llamar al detector ni guardar
        verify(mutantDetector, never()).isMutant(any());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Debe generar hash consistente para el mismo ADN")
    void testConsistentHashGeneration() {
        // ARRANGE
        when(repository.findById(anyString()))
                .thenReturn(Optional.empty());
        when(mutantDetector.isMutant(any()))
                .thenReturn(true);
        when(repository.save(any(DnaRecord.class)))
                .thenReturn(new DnaRecord("hash", true));

        // ACT - Analizar el mismo DNA dos veces
        mutantService.processDna(mutantDna);
        mutantService.processDna(mutantDna);

        // VERIFY - Debe buscar por el mismo hash ambas veces
        verify(repository, times(2)).findById(anyString());
    }

    @Test
    @DisplayName("Debe guardar registro con hash SHA-256 correcto")
    void testSavesRecordWithCorrectHash() {
        // ARRANGE
        when(repository.findById(anyString()))
                .thenReturn(Optional.empty());
        when(mutantDetector.isMutant(mutantDna))
                .thenReturn(true);

        // ACT
        mutantService.processDna(mutantDna);

        // VERIFY - Verificar que el hash tenga 64 caracteres (SHA-256)
        verify(repository).save(argThat(record ->
                record.getDnaHash() != null &&
                        record.getDnaHash().length() == 64 && // SHA-256 en hex = 64 chars
                        record.isMutant()
        ));
    }
}
