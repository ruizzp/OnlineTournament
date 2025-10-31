package com.tournament.controller;

import com.tournament.dto.GameDto;
import com.tournament.model.Game;
import com.tournament.model.Platform;
import com.tournament.repository.GameRepository;
import com.tournament.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
public class GameControllerClassicTest {

    /*
    @Mock
    private GameRepository gameRepository; // repository mockado
    */

    @Autowired
    private GameController gameController;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateGameClassic(){
        /*
        MockitoAnnotations.openMocks(this);
        Game mockGame = new Game();
        mockGame.setTitle("Chess");
        mockGame.setPlatform(Platform.PC);
        mockGame.setGenre("Strategy");
        when(gameRepository.existsByTitleAndPlatform(mockGame.getTitle(), mockGame.getPlatform())).thenReturn(false);
        when(gameRepository.save(any(Game.class))).thenReturn(mockGame);
        */

        GameDto createDto = new GameDto();
        createDto.setTitle("Chess");
        createDto.setPlatform(Platform.PC);
        createDto.setGenre("Strategy");


        ResponseEntity<GameDto> response = gameController.createGame(createDto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Chess", response.getBody().getTitle());

        // Verificar se o mock foi chamado
        //verify(gameRepository, times(1)).save(any(Game.class));
    }
}
