package com.tournament.dto;

import com.tournament.model.Platform;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Game response payload containing metadata about the tournament game.")
public class GameDto {
    
    @Schema(description = "Unique identifier of the game", example = "9d3a0d3d-1234-4b7a-8123-abcdef123456", accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    
    @Schema(description = "Game title", example = "Counter-Strike: Global Offensive", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Title is required")
    private String title;
    
    @Schema(description = "Game genre", example = "FPS", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Genre is required")
    private String genre;
    
    @Schema(description = "Primary platform where the game is played", example = "PC", requiredMode = Schema.RequiredMode.REQUIRED, implementation = Platform.class)
    @NotNull(message = "Platform is required")
    private Platform platform;

    public GameDto() {}

    public GameDto(String id, String title, String genre, Platform platform) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.platform = platform;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }
}