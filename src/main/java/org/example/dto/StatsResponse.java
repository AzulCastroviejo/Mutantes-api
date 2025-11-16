package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Estadísticas de verificaciones de ADN")
public class StatsResponse {

    @Schema(
            description = "Cantidad de ADN mutante verificado",
            example = "40",
            minimum = "0"
    )
    @JsonProperty("count_mutant_dna")
    private long countMutantDna;

    @Schema(
            description = "Cantidad de ADN humano (no mutante) verificado",
            example = "100",
            minimum = "0"
    )
    @JsonProperty("count_human_dna")
    private long countHumanDna;


    @Schema(
            description = "Ratio entre mutantes y humanos (mutantes / humanos)",
            example = "0.4"
    )
    private double ratio;
}
