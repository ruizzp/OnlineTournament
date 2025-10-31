package com.tournament.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorizationTests {
    private String getFirstGameId(String token) throws Exception {
        String response = mockMvc.perform(get("/api/games")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).get(0).get("id").asText();
    }

    private String getFirstTournamentName(String token) throws Exception {
        String response = mockMvc.perform(get("/api/tournaments")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).get(0).get("name").asText();
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String loginAndGetToken(String username, String password) throws Exception {
        String json = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
        String response = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).get("token").asText();
    }

    @Nested
    @DisplayName("Player role restrictions")
    class PlayerRole {
    @Test
    void playerCannotCreateTournament() throws Exception {
        String token = loginAndGetToken("player1", "password123");
        String tournamentJson = String.format("{\"name\":\"ValidTournByPlayer_%d\",\"maxPlayers\":4,\"gameTitle\":\"Counter-Strike: Global Offensive\",\"organizerUsername\":\"organizer1\"}", System.currentTimeMillis());
        mockMvc.perform(post("/api/tournaments")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(tournamentJson))
            .andExpect(status().isForbidden());
    }
    @Test
    void playerCannotDeleteGame() throws Exception {
        String token = loginAndGetToken("player1", "password123");
        String gameId = getFirstGameId(token);
        mockMvc.perform(delete("/api/games/" + gameId)
            .header("Authorization", "Bearer " + token))
            .andExpect(status().isForbidden());
    }
    }

    @Nested
    @DisplayName("Organizer role restrictions")
    class OrganizerRole {
    @Test
    void organizerCannotJoinOwnTournament() throws Exception {
        String token = loginAndGetToken("organizer1", "password123");
        String tournName = getFirstTournamentName(token);
        String participationJson = String.format("{\"tournamentName\":\"%s\",\"playerUsername\":\"organizer1\",\"score\":0}", tournName);
        mockMvc.perform(post("/api/participations")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(participationJson))
            .andExpect(status().isForbidden());
    }
    }

    @Nested
    @DisplayName("Admin role access")
    class AdminRole {
        @Test
        void adminCanAccessAllEndpoints() throws Exception {
            String token = loginAndGetToken("admin", "password123");
            mockMvc.perform(get("/api/users")
                    .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk());
            mockMvc.perform(get("/api/games")
                    .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk());
            mockMvc.perform(get("/api/tournaments")
                    .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void unauthenticatedUserCannotAccessProtectedEndpoints() throws Exception {
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isForbidden());
    }
}
