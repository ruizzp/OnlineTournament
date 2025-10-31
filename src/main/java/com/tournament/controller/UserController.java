package com.tournament.controller;

import com.tournament.dto.UserCreateDto;
import com.tournament.dto.UserDto;
import com.tournament.dto.UserRankingUpdateDto;
import com.tournament.model.enums.UserRole;
import com.tournament.service.UserService;
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
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@Tag(name = "Users", description = "Endpoints for managing platform users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all users", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of users",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PLAYER') or hasRole('ORGANIZER')")
    @Operation(summary = "Get user by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User found",
            content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<UserDto> getUserById(@PathVariable String id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN') or #username == principal.username or hasRole('PLAYER') or hasRole('ORGANIZER')")
    @Operation(summary = "Get user by username", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User found",
            content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        UserDto user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List users by role", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Users for the given role",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<List<UserDto>> getUsersByRole(@PathVariable UserRole role) {
        List<UserDto> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/rankings/top")
    @PreAuthorize("hasAnyRole('ADMIN','PLAYER','ORGANIZER')")
    @Operation(summary = "List top ranked users", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Top users by ranking",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<List<UserDto>> getTopRankedUsers(@RequestParam(defaultValue = "10") Integer limit) {
        List<UserDto> users = userService.getTopRankedUsers(limit);
        return ResponseEntity.ok(users);
    }
    
    @PostMapping
    @PreAuthorize("permitAll()")
    @Operation(summary = "Register a new user")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User created",
            content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserCreateDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User updated",
            content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<UserDto> updateUser(@PathVariable String id, @Valid @RequestBody UserCreateDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }
    
    @PatchMapping("/{id}/ranking")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user ranking", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ranking updated",
            content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<UserDto> updateUserRanking(@PathVariable String id, @Valid @RequestBody UserRankingUpdateDto rankingUpdateDto) {
    UserDto updatedUser = userService.updateUserRanking(id, rankingUpdateDto.getRanking());
        return ResponseEntity.ok(updatedUser);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete user", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "User removed", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}