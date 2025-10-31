package com.tournament.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Supported platforms that a game can run on.")
public enum Platform {
    PC("PC"),
    CONSOLE("Console"),
    MOBILE("Mobile"),
    WEB("Web"),
    CROSS_PLATFORM("Cross-Platform"),
    VR("VR");

    private final String displayName;

    Platform(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}