package com.tournament.service;

import com.tournament.dto.GameDto;
import com.tournament.model.Game;
import com.tournament.model.Platform;
import com.tournament.repository.GameRepository;
import com.tournament.mapper.GameMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameMapper gameMapper;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllGames() {
        
        Game mockGame = new Game();
        mockGame.setTitle("Chess");
        when(gameRepository.findAll()).thenReturn(Collections.singletonList(mockGame));

        GameDto mockGameDto = new GameDto();
        mockGameDto.setTitle("Chess");
        when(gameMapper.toDto(mockGame)).thenReturn(mockGameDto);

        
        List<GameDto> result = gameService.getAllGames();

        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Chess", result.get(0).getTitle());
        verify(gameRepository, times(1)).findAll();
    }

    @Test
    void testGetGameById() {
        
        String publicId = "game123";
        Game mockGame = new Game();
        mockGame.setPublicId(publicId);
        when(gameRepository.findByPublicId(publicId)).thenReturn(Optional.of(mockGame));

        GameDto mockGameDto = new GameDto();
        mockGameDto.setId(publicId);
        when(gameMapper.toDto(mockGame)).thenReturn(mockGameDto);

        
        GameDto result = gameService.getGameById(publicId);

        
        assertNotNull(result);
        assertEquals(publicId, result.getId());
        verify(gameRepository, times(1)).findByPublicId(publicId);
    }

    @Test
    void testCreateGame() {
        
        GameDto gameDto = new GameDto();
        gameDto.setTitle("Chess");
        gameDto.setPlatform(Platform.PC);

        Game mockGame = new Game();
        mockGame.setTitle("Chess");
        mockGame.setPlatform(Platform.PC);
        when(gameMapper.toEntity(gameDto)).thenReturn(mockGame);
        when(gameRepository.save(mockGame)).thenReturn(mockGame);

        GameDto mockGameDto = new GameDto();
        mockGameDto.setTitle("Chess");
        mockGameDto.setPlatform(Platform.PC);
        when(gameMapper.toDto(mockGame)).thenReturn(mockGameDto);

        
        GameDto result = gameService.createGame(gameDto);

        
        assertNotNull(result);
        assertEquals("Chess", result.getTitle());
        verify(gameRepository, times(1)).save(mockGame);
    }
}