package com.tournament.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Possible outcomes for a tournament match.")
public enum MatchResult {
    PLAYER1_WIN,
    PLAYER2_WIN,
    DRAW,
    PENDING
}