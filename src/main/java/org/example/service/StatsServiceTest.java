package org.example.service;

@ExtendWith(MockitoExtension.class)
public class StatsServiceTest {

    @Mock // 1. Creamos un "doble" falso del repositorio
    private DnaRecordRepository repository;

    @InjectMocks // 2. Creamos el servicio e inyectamos el mock de arriba
    private StatsService statsService;

    @Test
    void testGetStats_CalculaRatioCorrectamente() {
        // 3. Definimos el comportamiento del mock:
        // "Cuando llamen a countByIsMutant(true), respondé 40"
        when(repository.countByIsMutant(true)).thenReturn(40L);
        // "Cuando llamen a countByIsMutant(false), respondé 100"
        when(repository.countByIsMutant(false)).thenReturn(100L);

        // 4. Ejecutamos el método
        StatsResponse stats = statsService.getStats();

        // 5. Verificamos el resultado
        assertEquals(40, stats.getCountMutantDna());
        assertEquals(100, stats.getCountHumanDna());
        assertEquals(0.4, stats.getRatio()); // 40 / 100 = 0.4
    }

    @Test
    void testGetStats_ManejaDivisionPorCero() {
        // Caso borde: 5 mutantes, 0 humanos
        when(repository.countByIsMutant(true)).thenReturn(5L);
        when(repository.countByIsMutant(false)).thenReturn(0L);

        StatsResponse stats = statsService.getStats();

        assertEquals(5, stats.getCountMutantDna());
        assertEquals(0, stats.getCountHumanDna());
        assertEquals(0.0, stats.getRatio()); // Verificamos que no explote
    }

}
