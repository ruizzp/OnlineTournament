package com.tournament.controller;

import com.tournament.dto.MatchCreateDto;
import com.tournament.dto.MatchDto;
import com.tournament.model.enums.MatchResult;
import com.tournament.service.MatchService;
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
@RequestMapping("/api/matches")
@CrossOrigin(origins = "*")
@Tag(name = "Matches", description = "Endpoints for managing matches inside tournaments")
public class MatchController {
    
    @Autowired
    private MatchService matchService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','PLAYER','ORGANIZER')")
    @Operation(summary = "List all matches", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of matches",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = MatchDto.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<List<MatchDto>> getAllMatches() {
        List<MatchDto> matches = matchService.getAllMatches();
        return ResponseEntity.ok(matches);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PLAYER','ORGANIZER')")
    @Operation(summary = "Get match by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Match found",
            content = @Content(schema = @Schema(implementation = MatchDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Match not found", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<MatchDto> getMatchById(@PathVariable String id) {
        MatchDto match = matchService.getMatchById(id);
        return ResponseEntity.ok(match);
    }
    
    @GetMapping("/tournament/{tournamentId}")
    @PreAuthorize("hasAnyRole('ADMIN','PLAYER','ORGANIZER')")
    @Operation(summary = "List matches by tournament", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Matches for the tournament",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = MatchDto.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<List<MatchDto>> getMatchesByTournament(@PathVariable String tournamentId) {
        List<MatchDto> matches = matchService.getMatchesByTournament(tournamentId);
        return ResponseEntity.ok(matches);
    }
    
    @GetMapping("/tournament/{tournamentId}/round/{round}")
    @PreAuthorize("hasAnyRole('ADMIN','PLAYER','ORGANIZER')")
    @Operation(summary = "List matches by tournament and round", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Matches for the specified round",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = MatchDto.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<List<MatchDto>> getMatchesByTournamentAndRound(@PathVariable String tournamentId, @PathVariable Integer round) {
        List<MatchDto> matches = matchService.getMatchesByTournamentAndRound(tournamentId, round);
        return ResponseEntity.ok(matches);
    }
    
    @GetMapping("/player/{playerId}")
    @PreAuthorize("hasAnyRole('ADMIN','PLAYER','ORGANIZER')")
    @Operation(summary = "List matches for a player", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Matches where the player participates",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = MatchDto.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<List<MatchDto>> getMatchesByPlayer(@PathVariable String playerId) {
        List<MatchDto> matches = matchService.getMatchesByPlayer(playerId);
        return ResponseEntity.ok(matches);
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    @Operation(summary = "Create a match", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Match created",
            content = @Content(schema = @Schema(implementation = MatchDto.class))),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<MatchDto> createMatch(@Valid @RequestBody MatchCreateDto matchCreateDto) {
        MatchDto createdMatch = matchService.createMatch(matchCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMatch);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    @Operation(summary = "Update a match", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Match updated",
            content = @Content(schema = @Schema(implementation = MatchDto.class))),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Match not found", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<MatchDto> updateMatch(@PathVariable String id, @Valid @RequestBody MatchCreateDto matchCreateDto) {
        MatchDto updatedMatch = matchService.updateMatch(id, matchCreateDto);
        return ResponseEntity.ok(updatedMatch);
    }
    
    @PatchMapping("/{id}/result")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    @Operation(summary = "Update match result", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Match result updated",
            content = @Content(schema = @Schema(implementation = MatchDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid result", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Match not found", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<MatchDto> updateMatchResult(@PathVariable String id, @RequestBody MatchResult result) {
        MatchDto updatedMatch = matchService.updateMatchResult(id, result);
        return ResponseEntity.ok(updatedMatch);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a match", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Match removed", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Match not found", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Void> deleteMatch(@PathVariable String id) {
        matchService.deleteMatch(id);
        return ResponseEntity.noContent().build();
    }
}