package com.tournament.dto;

import com.tournament.model.enums.MatchResult;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Payload for creating or updating a match inside a tournament.")
public class MatchCreateDto {
    
    @Schema(description = "Tournament name that the match belongs to", example = "OrgTournTest_2025", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Tournament name is required")
    private String tournamentName;
    
    @Schema(description = "Tournament round number for this match", example = "1", minimum = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Round is required")
    private Integer round;
    
    @Schema(description = "Username of the first player", example = "player1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Player1 username is required")
    private String player1Username;
    
    @Schema(description = "Username of the second player", example = "player2", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Player2 username is required")
    private String player2Username;
    
    @Schema(description = "Match result. Defaults to PENDING if omitted", example = "PENDING", implementation = MatchResult.class)
    private MatchResult result;

    
    public MatchCreateDto() {}

    
    public MatchCreateDto(String tournamentName, Integer round, String player1Username, 
                         String player2Username, MatchResult result) {
        this.tournamentName = tournamentName;
        this.round = round;
        this.player1Username = player1Username;
        this.player2Username = player2Username;
        this.result = result;
    }

    
    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public String getPlayer1Username() {
        return player1Username;
    }

    public void setPlayer1Username(String player1Username) {
        this.player1Username = player1Username;
    }

    public String getPlayer2Username() {
        return player2Username;
    }

    public void setPlayer2Username(String player2Username) {
        this.player2Username = player2Username;
    }

    public MatchResult getResult() {
        return result;
    }

    public void setResult(MatchResult result) {
        this.result = result;
    }
}