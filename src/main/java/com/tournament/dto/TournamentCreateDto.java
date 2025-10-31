package com.tournament.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Payload for creating a tournament.")
public class TournamentCreateDto {
    
    @Schema(description = "Human-readable name of the tournament", example = "Fall Invitational", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Tournament name is required")
    private String name;
    
    @Schema(description = "Maximum number of players allowed", example = "16", minimum = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Max players is required")
    @Min(value = 2, message = "Tournament must have at least 2 players")
    private Integer maxPlayers;
    
    @Schema(description = "Title of the game that this tournament features", example = "Counter-Strike: Global Offensive", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Game title is required")
    private String gameTitle;
    
    @Schema(description = "Organizer's username", example = "organizer1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Organizer username is required")
    private String organizerUsername;

    
    public TournamentCreateDto() {}

    
    public TournamentCreateDto(String name, Integer maxPlayers, String gameTitle, String organizerUsername) {
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.gameTitle = gameTitle;
        this.organizerUsername = organizerUsername;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public String getOrganizerUsername() {
        return organizerUsername;
    }

    public void setOrganizerUsername(String organizerUsername) {
        this.organizerUsername = organizerUsername;
    }
}