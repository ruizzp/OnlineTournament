package com.tournament.controller;

import com.tournament.dto.ParticipationCreateDto;
import com.tournament.dto.ParticipationDto;
import com.tournament.dto.ParticipationScoreUpdateDto;
import com.tournament.service.ParticipationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participations")
@CrossOrigin(origins = "*")
@Tag(name = "Participations", description = "Endpoints for managing player participation in tournaments")
public class ParticipationController {
    
    @Autowired
    private ParticipationService participationService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','PLAYER','ORGANIZER')")
    @Operation(summary = "List all participations", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of participations",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ParticipationDto.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<List<ParticipationDto>> getAllParticipations() {
        List<ParticipationDto> participations = participationService.getAllParticipations();
        return ResponseEntity.ok(participations);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PLAYER','ORGANIZER')")
    @Operation(summary = "Get participation by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Participation found",
            content = @Content(schema = @Schema(implementation = ParticipationDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<ParticipationDto> getParticipationById(@PathVariable String id) {
        ParticipationDto participation = participationService.getParticipationById(id);
        return ResponseEntity.ok(participation);
    }
    
    @GetMapping("/tournament/{tournamentId}")
    @PreAuthorize("hasAnyRole('ADMIN','PLAYER','ORGANIZER')")
    @Operation(summary = "List participations by tournament", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Participations for the tournament",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ParticipationDto.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<List<ParticipationDto>> getParticipationsByTournament(@PathVariable String tournamentId) {
        List<ParticipationDto> participations = participationService.getParticipationsByTournament(tournamentId);
        return ResponseEntity.ok(participations);
    }
    
    @GetMapping("/player/{playerId}")
    @PreAuthorize("hasAnyRole('ADMIN','PLAYER','ORGANIZER')")
    @Operation(summary = "List participations for a player", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Participations for the player",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ParticipationDto.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<List<ParticipationDto>> getParticipationsByPlayer(@PathVariable String playerId) {
        List<ParticipationDto> participations = participationService.getParticipationsByPlayer(playerId);
        return ResponseEntity.ok(participations);
    }
    
    @GetMapping("/tournament/{tournamentId}/leaderboard")
    @PreAuthorize("hasAnyRole('ADMIN','PLAYER','ORGANIZER')")
    @Operation(summary = "Get tournament leaderboard", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Leaderboard ordered by score",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ParticipationDto.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<List<ParticipationDto>> getTournamentLeaderboard(@PathVariable String tournamentId) {
        List<ParticipationDto> leaderboard = participationService.getTournamentLeaderboard(tournamentId);
        return ResponseEntity.ok(leaderboard);
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','PLAYER')")
    @Operation(summary = "Register player in tournament", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Participation created",
            content = @Content(schema = @Schema(implementation = ParticipationDto.class))),
        @ApiResponse(responseCode = "400", description = "Validation error or business rule violation", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<ParticipationDto> createParticipation(@Valid @RequestBody ParticipationCreateDto participationCreateDto) {
        ParticipationDto createdParticipation = participationService.createParticipation(participationCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdParticipation);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update participation", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Participation updated",
            content = @Content(schema = @Schema(implementation = ParticipationDto.class))),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<ParticipationDto> updateParticipation(@PathVariable String id, @Valid @RequestBody ParticipationCreateDto participationCreateDto) {
        ParticipationDto updatedParticipation = participationService.updateParticipation(id, participationCreateDto);
        return ResponseEntity.ok(updatedParticipation);
    }
    
    @PatchMapping("/{id}/score")
    @Operation(summary = "Update participant score", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Score updated",
            content = @Content(schema = @Schema(implementation = ParticipationDto.class))),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<ParticipationDto> updateParticipationScore(@PathVariable String id, @Valid @RequestBody ParticipationScoreUpdateDto scoreUpdateDto) {
    ParticipationDto updatedParticipation = participationService.updateParticipationScore(id, scoreUpdateDto.getScore());
        return ResponseEntity.ok(updatedParticipation);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete participation", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Participation removed", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Void> deleteParticipation(@PathVariable String id) {
        participationService.deleteParticipation(id);
        return ResponseEntity.noContent().build();
    }
}