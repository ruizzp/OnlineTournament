package com.tournament.controller;

import com.tournament.dto.TournamentCreateDto;
import com.tournament.dto.TournamentDto;
import com.tournament.service.TournamentService;
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

class TournamentControllerTest {

    @Mock
    private TournamentService tournamentService;

    @InjectMocks
    private TournamentController tournamentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTournaments() {
        
        TournamentDto mockTournament = new TournamentDto();
        mockTournament.setId("tournament123");
        when(tournamentService.getAllTournaments()).thenReturn(Collections.singletonList(mockTournament));

        
        ResponseEntity<List<TournamentDto>> response = tournamentController.getAllTournaments();

        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("tournament123", response.getBody().get(0).getId());
        verify(tournamentService, times(1)).getAllTournaments();
    }

    @Test
    void testGetTournamentById() {
        TournamentDto mockTournament = new TournamentDto();
        mockTournament.setId("tournament123");
        when(tournamentService.getTournamentById("tournament123")).thenReturn(mockTournament);

        ResponseEntity<TournamentDto> response = tournamentController.getTournamentById("tournament123");

        if (response.getStatusCode() == HttpStatus.OK) {
            assertNotNull(response.getBody());
            assertEquals("tournament123", response.getBody().getId());
        } else {
            fail("Expected HTTP status OK");
        }
    }

    @Test
    void testCreateTournament() {
        
        TournamentCreateDto createDto = new TournamentCreateDto();
        createDto.setName("Chess Championship");
        createDto.setMaxPlayers(16);

        TournamentDto mockTournament = new TournamentDto();
        mockTournament.setId("tournament123");
        mockTournament.setName("Chess Championship");
        when(tournamentService.createTournament(createDto)).thenReturn(mockTournament);

        
        ResponseEntity<TournamentDto> response = tournamentController.createTournament(createDto);

        
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("tournament123", response.getBody().getId());
        assertEquals("Chess Championship", response.getBody().getName());
        verify(tournamentService, times(1)).createTournament(createDto);
    }
}