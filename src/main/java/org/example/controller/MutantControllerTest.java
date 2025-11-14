package org.example.controller;


import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import static javax.swing.UIManager.get;
import static org.springframework.web.servlet.function.ServerResponse.status;

@SpringBootTest
@AutoConfigureMockMvc
 class MutantControllerTest {
    @Autowired
    private MockMvc mockMvc; // El cliente HTTP falso

    @Autowired
    private DnaRecordRepository repository; // La base de datos H2 real

    @Autowired
    private ObjectMapper objectMapper; // Para convertir objetos a JSON

    // IMPORTANTE: Limpiamos la BD antes de CADA test
    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void testPostMutant_CuandoEsMutante_Retorna200() throws Exception {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        DnaRequest request = new DnaRequest();
        request.setDna(dna);

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()); // HTTP 200 OK
    }

    @Test
    void testPostMutant_CuandoEsHumano_Retorna403() throws Exception {
        String[] dna = {"ATGC", "GCTA", "TCGA", "CGAT"};
        DnaRequest request = new DnaRequest();
        request.setDna(dna);

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden()); // HTTP 403 Forbidden
    }

    @Test
    void testPostMutant_ConADNInvalido_Retorna403() throws Exception {
        String[] dna = {"ATGC", "GCTA", "TCGA", "CGZX"}; // 'Z' 'X'
        DnaRequest request = new DnaRequest();
        request.setDna(dna);

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden()); // HTTP 403 por IllegalArgumentException
    }

    @Test
    void testGetStats_RetornaStatsCorrectas() throws Exception {
        // 1. Creamos datos: 1 mutante y 1 humano
        DnaRequest mutantRequest = new DnaRequest();
        mutantRequest.setDna(new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"});

        DnaRequest humanRequest = new DnaRequest();
        humanRequest.setDna(new String[]{"ATGC", "GCTA", "TCGA", "CGAT"});

        // Hacemos los POST
        mockMvc.perform(post("/mutant").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(mutantRequest)));
        mockMvc.perform(post("/mutant").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(humanRequest)));

        // 2. Probamos el endpoint /stats
        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna", is(1)))
                .andExpect(jsonPath("$.count_human_dna", is(1)))
                .andExpect(jsonPath("$.ratio", is(1.0)));
    }

    @Test
    void testGetStats_SinDatos_RetornaCero() throws Exception {
        // Probamos /stats con la base de datos vacía
        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna", is(0)))
                .andExpect(jsonPath("$.count_human_dna", is(0)))
                .andExpect(jsonPath("$.ratio", is(0.0)));
    }
}
