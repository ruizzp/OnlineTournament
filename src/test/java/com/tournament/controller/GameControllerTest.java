package com.tournament.controller;

import com.tournament.dto.GameDto;
import com.tournament.model.Platform;
import com.tournament.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameControllerTest {

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllGames() {
        
        GameDto mockGame = new GameDto();
        mockGame.setId("game123");
        when(gameService.getAllGames()).thenReturn(Collections.singletonList(mockGame));

        
        ResponseEntity<List<GameDto>> response = gameController.getAllGames();

        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("game123", response.getBody().get(0).getId());
        verify(gameService, times(1)).getAllGames();
    }

    @Test
    void testGetGameById() {
        
        String gameId = "game123";
        GameDto mockGame = new GameDto();
        mockGame.setId(gameId);
        when(gameService.getGameById(gameId)).thenReturn(mockGame);

        
        ResponseEntity<GameDto> response = gameController.getGameById(gameId);

        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(gameId, response.getBody().getId());
        verify(gameService, times(1)).getGameById(gameId);
    }

    @Test
    void testCreateGame() {
        
        GameDto createDto = new GameDto();
        createDto.setTitle("Chess");
        createDto.setPlatform(Platform.PC);

        GameDto mockGame = new GameDto();
        mockGame.setId("game123");
        mockGame.setTitle("Chess");
        when(gameService.createGame(createDto)).thenReturn(mockGame);

        
        ResponseEntity<GameDto> response = gameController.createGame(createDto);

        
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("game123", response.getBody().getId());
        assertEquals("Chess", response.getBody().getTitle());
        verify(gameService, times(1)).createGame(createDto);
    }
}