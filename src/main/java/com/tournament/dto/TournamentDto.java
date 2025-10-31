package com.tournament.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Tournament response payload with associated metadata.")
public class TournamentDto {
    
    @Schema(description = "Unique identifier of the tournament", example = "e2f1bfb3-6a10-4c5d-ab63-aa9fa0e4f7de", accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    
    @Schema(description = "Tournament name", example = "Fall Invitational", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Tournament name is required")
    private String name;
    
    @Schema(description = "Maximum allowed players", example = "16", minimum = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Max players is required")
    @Min(value = 2, message = "Tournament must have at least 2 players")
    private Integer maxPlayers;
    
    @Schema(description = "Number of currently registered players", example = "8", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer currentPlayers;
    
    @Schema(description = "Game associated with the tournament", implementation = GameDto.class)
    private GameDto game;

    @Schema(description = "Organizer user details", implementation = UserDto.class)
    private UserDto organizer;

    
    public TournamentDto() {}

    
    public TournamentDto(String id, String name, Integer maxPlayers, Integer currentPlayers, 
                        GameDto game, UserDto organizer) {
        this.id = id;
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.currentPlayers = currentPlayers;
        this.game = game;
        this.organizer = organizer;
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getCurrentPlayers() {
        return currentPlayers;
    }

    public void setCurrentPlayers(Integer currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    public GameDto getGame() {
        return game;
    }

    public void setGame(GameDto game) {
        this.game = game;
    }

    public UserDto getOrganizer() {
        return organizer;
    }

    public void setOrganizer(UserDto organizer) {
        this.organizer = organizer;
    }
}