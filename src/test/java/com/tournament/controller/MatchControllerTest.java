package com.tournament.controller;

import com.tournament.dto.MatchCreateDto;
import com.tournament.dto.MatchDto;
import com.tournament.model.enums.MatchResult;
import com.tournament.service.MatchService;
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

class MatchControllerTest {

    @Mock
    private MatchService matchService;

    @InjectMocks
    private MatchController matchController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllMatches() {
        
        MatchDto mockMatch = new MatchDto();
        mockMatch.setId("match123");
        when(matchService.getAllMatches()).thenReturn(Collections.singletonList(mockMatch));

        
        ResponseEntity<List<MatchDto>> response = matchController.getAllMatches();

        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("match123", response.getBody().get(0).getId());
        verify(matchService, times(1)).getAllMatches();
    }

    @Test
    void testGetMatchById() {
        
        String matchId = "match123";
        MatchDto mockMatch = new MatchDto();
        mockMatch.setId(matchId);
        when(matchService.getMatchById(matchId)).thenReturn(mockMatch);

        
        ResponseEntity<MatchDto> response = matchController.getMatchById(matchId);

        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(matchId, response.getBody().getId());
        verify(matchService, times(1)).getMatchById(matchId);
    }

    @Test
    void testCreateMatch() {
        
        MatchCreateDto createDto = new MatchCreateDto();
        createDto.setTournamentName("Chess Championship");
        createDto.setPlayer1Username("player1");
        createDto.setPlayer2Username("player2");
        createDto.setRound(1);

        MatchDto mockMatch = new MatchDto();
        mockMatch.setId("match123");
        when(matchService.createMatch(createDto)).thenReturn(mockMatch);

        
        ResponseEntity<MatchDto> response = matchController.createMatch(createDto);

        
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("match123", response.getBody().getId());
        verify(matchService, times(1)).createMatch(createDto);
    }
}