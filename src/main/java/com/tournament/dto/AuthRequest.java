package com.tournament.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credentials used to authenticate a user and obtain a JWT token.")
public class AuthRequest {

    @Schema(description = "Unique username for the account", example = "player1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Username is required")
    private String username;

    @Schema(description = "Account password", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Password is required")
    private String password;

    public AuthRequest() {
    }

    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
