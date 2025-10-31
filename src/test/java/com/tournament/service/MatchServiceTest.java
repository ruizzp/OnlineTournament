package com.tournament.service;

import com.tournament.dto.MatchCreateDto;
import com.tournament.dto.MatchDto;
import com.tournament.model.Match;
import com.tournament.model.Tournament;
import com.tournament.model.User;
import com.tournament.model.enums.MatchResult;
import com.tournament.repository.MatchRepository;
import com.tournament.repository.ParticipationRepository;
import com.tournament.repository.TournamentRepository;
import com.tournament.repository.UserRepository;
import com.tournament.mapper.MatchMapper;
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

class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ParticipationRepository participationRepository;

    @Mock
    private MatchMapper matchMapper;

    @InjectMocks
    private MatchService matchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllMatches() {
        
        Match mockMatch = new Match();
        mockMatch.setPublicId("match123");
        when(matchRepository.findAll()).thenReturn(Collections.singletonList(mockMatch));

        MatchDto mockMatchDto = new MatchDto();
        mockMatchDto.setId("match123");
        when(matchMapper.toDto(mockMatch)).thenReturn(mockMatchDto);

        
        List<MatchDto> result = matchService.getAllMatches();

        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("match123", result.get(0).getId());
        verify(matchRepository, times(1)).findAll();
    }

    @Test
    void testCreateMatch() {
        
        MatchCreateDto createDto = new MatchCreateDto();
        createDto.setTournamentName("Chess Championship");
        createDto.setPlayer1Username("player1");
        createDto.setPlayer2Username("player2");
        createDto.setRound(1);

        Tournament mockTournament = new Tournament();
        mockTournament.setName("Chess Championship");
        when(tournamentRepository.findByName("Chess Championship")).thenReturn(Optional.of(mockTournament));

        User mockPlayer1 = new User();
        mockPlayer1.setUsername("player1");
        when(userRepository.findByUsername("player1")).thenReturn(Optional.of(mockPlayer1));

        User mockPlayer2 = new User();
        mockPlayer2.setUsername("player2");
        when(userRepository.findByUsername("player2")).thenReturn(Optional.of(mockPlayer2));

        when(participationRepository.existsByTournamentAndPlayer(mockTournament, mockPlayer1)).thenReturn(true);
        when(participationRepository.existsByTournamentAndPlayer(mockTournament, mockPlayer2)).thenReturn(true);

        Match mockMatch = new Match();
        mockMatch.setPublicId("match123");
        when(matchRepository.save(any(Match.class))).thenReturn(mockMatch);

        MatchDto mockMatchDto = new MatchDto();
        mockMatchDto.setId("match123");
        when(matchMapper.toDto(mockMatch)).thenReturn(mockMatchDto);

        
        MatchDto result = matchService.createMatch(createDto);

        
        assertNotNull(result);
        assertEquals("match123", result.getId());
        verify(matchRepository, times(1)).save(any(Match.class));
    }
}