package com.tournament.controller;

import com.tournament.dto.ParticipationCreateDto;
import com.tournament.dto.ParticipationDto;
import com.tournament.service.ParticipationService;
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

class ParticipationControllerTest {

    @Mock
    private ParticipationService participationService;

    @InjectMocks
    private ParticipationController participationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllParticipations() {
        
        ParticipationDto mockParticipation = new ParticipationDto();
        mockParticipation.setId("participation123");
        when(participationService.getAllParticipations()).thenReturn(Collections.singletonList(mockParticipation));

        
        ResponseEntity<List<ParticipationDto>> response = participationController.getAllParticipations();

        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("participation123", response.getBody().get(0).getId());
        verify(participationService, times(1)).getAllParticipations();
    }

    @Test
    void testGetParticipationById() {
        
        String participationId = "participation123";
        ParticipationDto mockParticipation = new ParticipationDto();
        mockParticipation.setId(participationId);
        when(participationService.getParticipationById(participationId)).thenReturn(mockParticipation);

        
        ResponseEntity<ParticipationDto> response = participationController.getParticipationById(participationId);

        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(participationId, response.getBody().getId());
        verify(participationService, times(1)).getParticipationById(participationId);
    }

    @Test
    void testCreateParticipation() {
        
        ParticipationCreateDto createDto = new ParticipationCreateDto();
        createDto.setTournamentName("Chess Championship");
        createDto.setPlayerUsername("player1");
        createDto.setScore(100);

        ParticipationDto mockParticipation = new ParticipationDto();
        mockParticipation.setId("participation123");
        when(participationService.createParticipation(createDto)).thenReturn(mockParticipation);

        
        ResponseEntity<ParticipationDto> response = participationController.createParticipation(createDto);

        
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("participation123", response.getBody().getId());
        verify(participationService, times(1)).createParticipation(createDto);
    }

    @Test
    void testGetAllParticipationsAndVerifyServiceCall() {
        ParticipationDto mockParticipation = new ParticipationDto();
        mockParticipation.setId("participation123");
        when(participationService.getAllParticipations()).thenReturn(Collections.singletonList(mockParticipation));

        ResponseEntity<List<ParticipationDto>> response = participationController.getAllParticipations();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("participation123", response.getBody().get(0).getId());

        verify(participationService, times(1)).getAllParticipations();
        verifyNoMoreInteractions(participationService);
    }
}