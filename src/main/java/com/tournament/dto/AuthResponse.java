package com.tournament.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response returned after successful authentication.")
public class AuthResponse {

    @Schema(description = "JWT token that must be used in the Authorization header", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Role associated with the authenticated user", example = "PLAYER")
    private String role;

    @Schema(description = "Username of the authenticated user", example = "player1")
    private String username;

    public AuthResponse() {
    }

    public AuthResponse(String token, String role, String username) {
        this.token = token;
        this.role = role;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
