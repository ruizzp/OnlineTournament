package com.tournament.controller;

import com.tournament.dto.GameDto;
import com.tournament.model.Platform;
import com.tournament.service.GameService;
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
@RequestMapping("/api/games")
@CrossOrigin(origins = "*")
@Tag(name = "Games", description = "Endpoints for managing games available for tournaments")
public class GameController {
    
    @Autowired
    private GameService gameService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','PLAYER','ORGANIZER')")
    @Operation(summary = "List all games", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of games",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = GameDto.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<List<GameDto>> getAllGames() {
        List<GameDto> games = gameService.getAllGames();
        return ResponseEntity.ok(games);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PLAYER','ORGANIZER')")
    @Operation(summary = "Get game by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Game found",
            content = @Content(schema = @Schema(implementation = GameDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Game not found", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<GameDto> getGameById(@PathVariable String id) {
        GameDto game = gameService.getGameById(id);
        return ResponseEntity.ok(game);
    }
    
    @GetMapping("/genre/{genre}")
    @PreAuthorize("hasAnyRole('ADMIN','PLAYER','ORGANIZER')")
    @Operation(summary = "List games by genre", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Games filtered by genre",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = GameDto.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<List<GameDto>> getGamesByGenre(@PathVariable String genre) {
        List<GameDto> games = gameService.getGamesByGenre(genre);
        return ResponseEntity.ok(games);
    }
    
    @GetMapping("/platform/{platform}")
    @PreAuthorize("hasAnyRole('ADMIN','PLAYER','ORGANIZER')")
    @Operation(summary = "List games by platform", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Games filtered by platform",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = GameDto.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<List<GameDto>> getGamesByPlatform(@PathVariable Platform platform) {
        List<GameDto> games = gameService.getGamesByPlatform(platform);
        return ResponseEntity.ok(games);
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','PLAYER','ORGANIZER')")
    @Operation(summary = "Search games by title", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Games matching the title",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = GameDto.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<List<GameDto>> searchGamesByTitle(@RequestParam String title) {
        List<GameDto> games = gameService.searchGamesByTitle(title);
        return ResponseEntity.ok(games);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a game", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Game created",
            content = @Content(schema = @Schema(implementation = GameDto.class))),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<GameDto> createGame(@Valid @RequestBody GameDto gameDto) {
        GameDto createdGame = gameService.createGame(gameDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGame);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a game", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Game updated",
            content = @Content(schema = @Schema(implementation = GameDto.class))),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Game not found", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<GameDto> updateGame(@PathVariable String id, @Valid @RequestBody GameDto gameDto) {
        GameDto updatedGame = gameService.updateGame(id, gameDto);
        return ResponseEntity.ok(updatedGame);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a game", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Game removed", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))) ,
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Game not found", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Void> deleteGame(@PathVariable String id) {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }
}