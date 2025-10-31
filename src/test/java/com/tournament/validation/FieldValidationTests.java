package com.tournament.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FieldValidationTests {

    @Autowired
    private MockMvc mockMvc;

    private String loginAndGetToken(String username, String password) throws Exception {
        String json = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
        String response = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return new ObjectMapper().readTree(response).get("token").asText();
    }

    @Test
    @DisplayName("Test tournament creation with invalid fields")
    void testTournamentCreationWithInvalidFields() throws Exception {
        String token = loginAndGetToken("organizer1", "password123");
        String invalidTournamentJson = "{" +
                "\"name\":\"\"," +
                "\"maxPlayers\":-1," + 
                "\"gameTitle\":\"\"," + 
                "\"organizerUsername\":\"organizer1\"}"; 

        var result = mockMvc.perform(post("/api/tournaments")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidTournamentJson))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
        assertNotNull(result.getResponse().getContentAsString());
        assertTrue(result.getResponse().getContentAsString().contains("error"));
    }

    @Test
    @DisplayName("Test tournament creation with valid fields")
    void testTournamentCreationWithValidFields() throws Exception {
        String token = loginAndGetToken("organizer1", "password123");
        String validTournamentJson = "{" +
                "\"name\":\"Valid Tournament\"," +
                "\"maxPlayers\":8," +
                "\"gameTitle\":\"Counter-Strike: Global Offensive\"," +
                "\"organizerUsername\":\"organizer1\"}";

        mockMvc.perform(post("/api/tournaments")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validTournamentJson))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Test game creation with valid fields")
    void testGameCreationWithValidFields() throws Exception {
        String token = loginAndGetToken("admin", "password123");
        String validGameJson = "{" +
                "\"title\":\"APITestGame\"," +
                "\"genre\":\"FPS\"," +
                "\"platform\":\"PC\"}";

        mockMvc.perform(post("/api/games")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validGameJson))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Test game creation with invalid fields")
    void testGameCreationWithInvalidFields() throws Exception {
        String token = loginAndGetToken("admin", "password123");
        String invalidGameJson = "{" +
                "\"title\":\"\"," + 
                "\"genre\":\"\"," + 
                "\"platform\":\"INVALID\"}"; 

        mockMvc.perform(post("/api/games")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidGameJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test match creation with valid fields")
    void testMatchCreationWithValidFields() throws Exception {
        String token = loginAndGetToken("organizer1", "password123");
        String validMatchJson = "{" +
                "\"tournamentName\":\"CS:GO Championship\"," +
                "\"tournamentId\":\"1\"," +
                "\"player1Username\":\"player1\"," +
                "\"player2Username\":\"player2\"," +
                "\"round\":1," +
                "\"result\":\"PENDING\"}";

        mockMvc.perform(post("/api/matches")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validMatchJson))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Test match creation with invalid fields")
    void testMatchCreationWithInvalidFields() throws Exception {
        String token = loginAndGetToken("organizer1", "password123");
        String invalidMatchJson = "{" +
                "\"tournamentName\":\"\"," + 
                "\"tournamentId\":\"\"," + 
                "\"player1Username\":\"\"," + 
                "\"player2Username\":\"\"," + 
                "\"round\":-1," + 
                "\"result\":\"INVALID\"}"; 

        mockMvc.perform(post("/api/matches")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidMatchJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test participation creation with valid fields")
    void testParticipationCreationWithValidFields() throws Exception {
        String adminToken = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "username": "admin",
                        "password": "password123"
                    }
                """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String newPlayerUsername = "player99";

        mockMvc.perform(post("/api/users")
                .header("Authorization", "Bearer " + extractToken(adminToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "username": "player99",
                        "password": "password123",
                        "role": "PLAYER",
                        "ranking": 0
                    }
                """))
                .andExpect(status().isCreated());

        String player99Token = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "username": "player99",
                        "password": "password123"
                    }
                """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        mockMvc.perform(post("/api/participations")
                .header("Authorization", "Bearer " + extractToken(player99Token))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "tournamentName": "CS:GO Championship",
                        "playerUsername": "player99",
                        "score": 0
                    }
                """))
                .andExpect(status().isCreated());
    }

    private String extractToken(String responseContent) throws Exception {
        return new ObjectMapper().readTree(responseContent).get("token").asText();
    }
}