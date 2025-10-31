package com.tournament.dto;

import com.tournament.model.enums.MatchResult;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Match response payload containing participants and outcome.")
public class MatchDto {
    
    @Schema(description = "Unique identifier of the match", example = "4a8fdc9d-0fd6-4acd-a6af-1b33f5d660c7", accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @Schema(description = "Tournament round number", example = "2", minimum = "1")
    private Integer round;

    @Schema(description = "Result of the match", example = "PLAYER1_WIN", implementation = MatchResult.class)
    private MatchResult result;
    
    @Schema(description = "Tournament details for the match", implementation = TournamentDto.class)
    private TournamentDto tournament;

    @Schema(description = "First player details", implementation = UserDto.class)
    private UserDto player1;

    @Schema(description = "Second player details", implementation = UserDto.class)
    private UserDto player2;

    
    public MatchDto() {}

    
    public MatchDto(String id, Integer round, MatchResult result, 
                   TournamentDto tournament, UserDto player1, UserDto player2) {
        this.id = id;
        this.round = round;
        this.result = result;
        this.tournament = tournament;
        this.player1 = player1;
        this.player2 = player2;
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public MatchResult getResult() {
        return result;
    }

    public void setResult(MatchResult result) {
        this.result = result;
    }

    public TournamentDto getTournament() {
        return tournament;
    }

    public void setTournament(TournamentDto tournament) {
        this.tournament = tournament;
    }

    public UserDto getPlayer1() {
        return player1;
    }

    public void setPlayer1(UserDto player1) {
        this.player1 = player1;
    }

    public UserDto getPlayer2() {
        return player2;
    }

    public void setPlayer2(UserDto player2) {
        this.player2 = player2;
    }
}