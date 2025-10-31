package com.tournament.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Payload for registering a player in a tournament.")
public class ParticipationCreateDto {
    
    @Schema(description = "Tournament name where the player will participate", example = "OrgTournTest_2025", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Tournament name is required")
    private String tournamentName;
    
    @Schema(description = "Username of the player joining the tournament", example = "player1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Player username is required")
    private String playerUsername;
    
    @Schema(description = "Initial score assigned to the player", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Score is required")
    private Integer score;

    
    public ParticipationCreateDto() {}

    
    public ParticipationCreateDto(String tournamentName, String playerUsername, Integer score) {
        this.tournamentName = tournamentName;
        this.playerUsername = playerUsername;
        this.score = score;
    }

    
    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public String getPlayerUsername() {
        return playerUsername;
    }

    public void setPlayerUsername(String playerUsername) {
        this.playerUsername = playerUsername;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}