package com.tournament.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Participation response payload linking tournaments and players.")
public class ParticipationDto {
    
    @Schema(description = "Unique identifier of the participation", example = "f4c8cc5b-96f4-43fd-9ad2-5123f8f7421a", accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @Schema(description = "Timestamp when the player joined the tournament", type = "string", format = "date-time", example = "2025-10-03T12:34:56")
    private LocalDateTime joinDate;

    @Schema(description = "Current score for the player inside the tournament", example = "120")
    private Integer score;
    
    @Schema(description = "Tournament details", implementation = TournamentDto.class)
    private TournamentDto tournament;

    @Schema(description = "Player details", implementation = UserDto.class)
    private UserDto player;

    
    public ParticipationDto() {}

    
    public ParticipationDto(String id, LocalDateTime joinDate, Integer score, 
                           TournamentDto tournament, UserDto player) {
        this.id = id;
        this.joinDate = joinDate;
        this.score = score;
        this.tournament = tournament;
        this.player = player;
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public TournamentDto getTournament() {
        return tournament;
    }

    public void setTournament(TournamentDto tournament) {
        this.tournament = tournament;
    }

    public UserDto getPlayer() {
        return player;
    }

    public void setPlayer(UserDto player) {
        this.player = player;
    }
}