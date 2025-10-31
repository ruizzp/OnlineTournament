package com.tournament.controller;

import com.tournament.dto.AuthRequest;
import com.tournament.dto.AuthResponse;
import com.tournament.model.User;
import com.tournament.repository.UserRepository;
import com.tournament.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user login and token generation")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    @Operation(
        summary = "Authenticate user",
        description = "Authenticates a user using username and password and returns a JWT token."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User authenticated successfully",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "Missing credentials",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Invalid credentials",
            content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        String username = authRequest.getUsername();
        String password = authRequest.getPassword();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByUsername(username).orElseThrow();
            String token = jwtUtil.generateToken(username, user.getRole().name());
            AuthResponse response = new AuthResponse(token, user.getRole().name(), userDetails.getUsername());
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
    }
}
