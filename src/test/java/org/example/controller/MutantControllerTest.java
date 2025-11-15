package controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.MutantController;
import org.example.dto.DnaRequest;
import org.example.dto.StatsResponse;
import org.example.repository.DnaRecordRepository;
import org.example.service.MutantService;
import org.example.service.StatsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
// Para que encuentre .post() y .get() (y así arreglar el error de .contentType())
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

// Para que encuentre .status() y .jsonPath() (y así arreglar el error de .status())
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Para que puedas usar "is()" dentro de tus jsonPath

import static org.hamcrest.Matchers.is;

import static javax.swing.UIManager.get;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.web.servlet.function.ServerResponse.status;

@WebMvcTest(MutantController.class)
 class MutantControllerTest {
    @Autowired
private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MutantService mutantService;

    @MockBean
    private StatsService statsService;

    @Test
    @DisplayName("POST /mutant debe retornar 200 OK para ADN mutante")
    void testCheckMutantReturns200ForMutant() throws Exception {
        // ARRANGE
        String[] mutantDna = {
                "ATGCGA", "CAGTGC", "TTATGT",
                "AGAAGG", "CCCCTA", "TCACTG"
        };
        DnaRequest request = new DnaRequest();
        request.setDna(mutantDna);

        when(mutantService.processDna(any(String[].class)))
                .thenReturn(true); // Mock: es mutante

        // ACT & ASSERT
        mockMvc.perform(
                        post("/mutant")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk()); // 200 OK
    }

    @Test
    @DisplayName("POST /mutant debe retornar 403 Forbidden para ADN humano")
    void testCheckMutantReturns403ForHuman() throws Exception {
        // ARRANGE
        String[] humanDna = {
                "ATGCGA", "CAGTGC", "TTATTT",
                "AGACGG", "GCGTCA", "TCACTG"
        };
        DnaRequest request = new DnaRequest();
        request.setDna(humanDna);

        when(mutantService.processDna(any(String[].class)))
                .thenReturn(false); // Mock: es humano

        // ACT & ASSERT
        mockMvc.perform(
                        post("/mutant")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isForbidden()); // 403 Forbidden
    }

    @Test
    @DisplayName("POST /mutant debe retornar 403 para ADN inválido")
    void testCheckMutantReturns403ForInvalidDna() throws Exception {
        // ARRANGE
        String[] invalidDna = {
                "ATXCGA", "CAGTGC", "TTATGT",
                "AGAAGG", "CCCCTA", "TCACTG"
        };
        DnaRequest request = new DnaRequest();
        request.setDna(invalidDna);

        when(mutantService.processDna(any(String[].class)))
                .thenThrow(new IllegalArgumentException("DNA inválido"));

        // ACT & ASSERT
        mockMvc.perform(
                        post("/mutant")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isForbidden()); // 403
    }

    @Test
    @DisplayName("POST /mutant debe rechazar body vacío")
    void testCheckMutantRejectsEmptyBody() throws Exception {
        // ACT & ASSERT
        mockMvc.perform(
                        post("/mutant")
                                .contentType(MediaType.APPLICATION_JSON)
                        // Sin .content() = body vacío
                )
                .andExpect(status().isBadRequest()); // 400 Bad Request
    }

    @Test
    @DisplayName("GET /stats debe retornar estadísticas correctamente")
    void testGetStatsReturnsCorrectData() throws Exception {
        // ARRANGE
        StatsResponse statsResponse = new StatsResponse(40, 100, 0.4);
        when(statsService.getStats()).thenReturn(statsResponse);

        // ACT & ASSERT
        mockMvc.perform(
                        get("/stats")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna").value(40))
                .andExpect(jsonPath("$.count_human_dna").value(100))
                .andExpect(jsonPath("$.ratio").value(0.4));
    }

    @Test
    @DisplayName("GET /stats debe retornar 200 OK incluso sin datos")
    void testGetStatsReturns200WithNoData() throws Exception {
        // ARRANGE
        StatsResponse statsResponse = new StatsResponse(0, 0, 0.0);
        when(statsService.getStats()).thenReturn(statsResponse);

        // ACT & ASSERT
        mockMvc.perform(
                        get("/stats")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna").value(0))
                .andExpect(jsonPath("$.count_human_dna").value(0))
                .andExpect(jsonPath("$.ratio").value(0.0));
    }

    @Test
    @DisplayName("POST /mutant debe aceptar Content-Type application/json")
    void testCheckMutantAcceptsJsonContentType() throws Exception {
        // ARRANGE
        String[] mutantDna = {
                "ATGCGA", "CAGTGC", "TTATGT",
                "AGAAGG", "CCCCTA", "TCACTG"
        };
        DnaRequest request = new DnaRequest();
        request.setDna(mutantDna);

        when(mutantService.processDna(any(String[].class)))
                .thenReturn(true);

        // ACT & ASSERT
        mockMvc.perform(
                        post("/mutant")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /mutant debe procesar múltiples requests correctamente")
    void testCheckMutantHandlesMultipleRequests() throws Exception {
        // ARRANGE
        String[] mutantDna = {
                "ATGCGA", "CAGTGC", "TTATGT",
                "AGAAGG", "CCCCTA", "TCACTG"
        };
        DnaRequest request = new DnaRequest();
        request.setDna(mutantDna);

        when(mutantService.processDna(any(String[].class)))
                .thenReturn(true);

        // ACT - Hacer 3 requests
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(
                            post("/mutant")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isOk());
        }

        // VERIFY - Se llamó 3 veces al servicio
        verify(mutantService, times(3)).processDna(any(String[].class));
    }
}
