package com.tournament.service;

import com.tournament.dto.ParticipationCreateDto;
import com.tournament.dto.ParticipationDto;
import com.tournament.model.Participation;
import com.tournament.model.Tournament;
import com.tournament.model.User;
import com.tournament.model.enums.UserRole;
import com.tournament.repository.ParticipationRepository;
import com.tournament.repository.TournamentRepository;
import com.tournament.repository.UserRepository;
import com.tournament.mapper.ParticipationMapper;
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

class ParticipationServiceTest {

    @Mock
    private ParticipationRepository participationRepository;

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ParticipationMapper participationMapper;

    @Mock
    private TournamentService tournamentService;

    @InjectMocks
    private ParticipationService participationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllParticipations() {
        
        Participation mockParticipation = new Participation();
        mockParticipation.setPublicId("participation123");
        when(participationRepository.findAll()).thenReturn(Collections.singletonList(mockParticipation));

        ParticipationDto mockParticipationDto = new ParticipationDto();
        mockParticipationDto.setId("participation123");
        when(participationMapper.toDto(mockParticipation)).thenReturn(mockParticipationDto);

        
        List<ParticipationDto> result = participationService.getAllParticipations();

        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("participation123", result.get(0).getId());
        verify(participationRepository, times(1)).findAll();
    }

    @Test
    void testCreateParticipation() {
        
        ParticipationCreateDto createDto = new ParticipationCreateDto();
        createDto.setTournamentName("Chess Championship");
        createDto.setPlayerUsername("player1");
        createDto.setScore(100);

        Tournament mockTournament = new Tournament();
        mockTournament.setName("Chess Championship");
        mockTournament.setCurrentPlayers(0);
        mockTournament.setMaxPlayers(16);
        mockTournament.setPublicId("tournament123"); 
        User mockOrganizer = new User();
        mockOrganizer.setId(1L);
        mockTournament.setOrganizer(mockOrganizer);
        when(tournamentRepository.findByName("Chess Championship")).thenReturn(Optional.of(mockTournament));

        User mockPlayer = new User();
        mockPlayer.setUsername("player1");
        mockPlayer.setRole(UserRole.PLAYER);
        when(userRepository.findByUsername("player1")).thenReturn(Optional.of(mockPlayer));

        Participation mockParticipation = new Participation();
        mockParticipation.setPublicId("participation123");
        when(participationRepository.save(any(Participation.class))).thenReturn(mockParticipation);

        ParticipationDto mockParticipationDto = new ParticipationDto();
        mockParticipationDto.setId("participation123");
        when(participationMapper.toDto(mockParticipation)).thenReturn(mockParticipationDto);

        
        ParticipationDto result = participationService.createParticipation(createDto);

        
        assertNotNull(result);
        assertEquals("participation123", result.getId());
        verify(participationRepository, times(1)).save(any(Participation.class));
        verify(tournamentService, times(1)).incrementCurrentPlayers("tournament123"); 
    }
}