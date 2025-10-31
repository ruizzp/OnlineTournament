package com.tournament.dto;

import com.tournament.model.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "User response payload with role and ranking information.")
public class UserDto {
    
    @Schema(description = "Unique identifier of the user", example = "6cfe1d3c-9f8a-4f6e-bc3a-7a54b0df77a0", accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    
    @Schema(description = "Username containing only alphanumeric characters", example = "player1", requiredMode = Schema.RequiredMode.REQUIRED, pattern = "^[a-zA-Z0-9]+$")
    @NotBlank(message = "Username is required")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username may only contain letters and numbers")
    private String username;
    
    @Schema(description = "Role assigned to the user", example = "ADMIN", requiredMode = Schema.RequiredMode.REQUIRED, implementation = UserRole.class)
    @NotNull(message = "Role is required")
    private UserRole role;
    
    @Schema(description = "Current ranking of the user", example = "250")
    private Integer ranking;

    
    public UserDto() {}

    
    public UserDto(String id, String username, UserRole role, Integer ranking) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.ranking = ranking;
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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