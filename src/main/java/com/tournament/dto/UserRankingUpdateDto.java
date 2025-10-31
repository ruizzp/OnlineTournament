package com.tournament.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Payload for updating a user's ranking.")
public class UserRankingUpdateDto {

    @Schema(description = "New ranking value", example = "250", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Ranking is required")
    private Integer ranking;

    public UserRankingUpdateDto() {
    }

    public UserRankingUpdateDto(Integer ranking) {
        this.ranking = ranking;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }
}
