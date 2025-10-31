package com.tournament.controller;

import com.tournament.dto.TournamentCreateDto;
import com.tournament.dto.TournamentDto;
import com.tournament.service.TournamentService;
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
@RequestMapping("/api/tournaments")
@CrossOrigin(origins = "*")
@Tag(name = "Tournaments", description = "Endpoints for discovering and managing tournaments")
public class TournamentController {
    
    @Autowired
    private TournamentService tournamentService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','PLAYER','ORGANIZER')")
    @Operation(summary = "List all tournaments", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of tournaments",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TournamentDto.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<List<TournamentDto>> getAllTournaments() {
        List<TournamentDto> tournaments = tournamentService.getAllTournaments();
        return ResponseEntity.ok(tournaments);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PLAYER','ORGANIZER')")
    @Operation(summary = "Get tournament by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tournament found",
            content = @Content(schema = @Schema(implementation = TournamentDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Tournament not found", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<TournamentDto> getTournamentById(@PathVariable String id) {
        TournamentDto tournament = tournamentService.getTournamentById(id);
        return ResponseEntity.ok(tournament);
    }
    
    @GetMapping("/organizer/{organizerId}")
    @PreAuthorize("hasAnyRole('ADMIN','PLAYER','ORGANIZER')")
    @Operation(summary = "List tournaments by organizer", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tournaments for the organizer",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TournamentDto.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<List<TournamentDto>> getTournamentsByOrganizer(@PathVariable String organizerId) {
        List<TournamentDto> tournaments = tournamentService.getTournamentsByOrganizer(organizerId);
        return ResponseEntity.ok(tournaments);
    }
    
    @GetMapping("/available")
    @PreAuthorize("hasAnyRole('ADMIN','PLAYER','ORGANIZER')")
    @Operation(summary = "List open tournaments", description = "Returns tournaments that haven't reached capacity.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Available tournaments",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TournamentDto.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<List<TournamentDto>> getAvailableTournaments() {
        List<TournamentDto> tournaments = tournamentService.getAvailableTournaments();
        return ResponseEntity.ok(tournaments);
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    @Operation(summary = "Create tournament", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Tournament created",
            content = @Content(schema = @Schema(implementation = TournamentDto.class))),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<TournamentDto> createTournament(@Valid @RequestBody TournamentCreateDto tournamentCreateDto) {
        TournamentDto createdTournament = tournamentService.createTournament(tournamentCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTournament);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    @Operation(summary = "Update tournament", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tournament updated",
            content = @Content(schema = @Schema(implementation = TournamentDto.class))),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Tournament not found", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<TournamentDto> updateTournament(@PathVariable String id, @Valid @RequestBody TournamentCreateDto tournamentCreateDto) {
        TournamentDto updatedTournament = tournamentService.updateTournament(id, tournamentCreateDto);
        return ResponseEntity.ok(updatedTournament);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete tournament", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Tournament removed", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Tournament not found", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Void> deleteTournament(@PathVariable String id) {
        tournamentService.deleteTournament(id);
        return ResponseEntity.noContent().build();
    }
}