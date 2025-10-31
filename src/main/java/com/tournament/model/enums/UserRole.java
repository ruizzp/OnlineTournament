package com.tournament.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Roles supported by the platform.")
public enum UserRole {
    ADMIN,
    PLAYER,
    ORGANIZER
}