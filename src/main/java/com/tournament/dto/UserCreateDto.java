package com.tournament.dto;

import com.tournament.model.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Payload for creating a new user account.")
public class UserCreateDto {
    
    @Schema(description = "Unique username containing only letters and numbers", example = "player42", requiredMode = Schema.RequiredMode.REQUIRED, pattern = "^[a-zA-Z0-9]+$")
    @NotBlank(message = "Username is required")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username may only contain letters and numbers")
    private String username;
    
    @Schema(description = "Clear-text password that will be hashed on creation", example = "StrongP@ssw0rd", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 6)
    @NotBlank(message = "Password is required")
    private String password;
    
    @Schema(description = "Role assigned to the user", example = "PLAYER", requiredMode = Schema.RequiredMode.REQUIRED, implementation = UserRole.class)
    @NotNull(message = "Role is required")
    private UserRole role;
    
    @Schema(description = "Initial ranking for the user (optional)", example = "100")
    private Integer ranking;

    
    public UserCreateDto() {}

    
    public UserCreateDto(String username, String password, UserRole role, Integer ranking) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.ranking = ranking;
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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }
}