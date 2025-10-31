package com.tournament.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Payload for updating a participant's score.")
public class ParticipationScoreUpdateDto {

    @Schema(description = "New score value for the participant", example = "150", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Score is required")
    private Integer score;

    public ParticipationScoreUpdateDto() {
    }

    public ParticipationScoreUpdateDto(Integer score) {
        this.score = score;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
