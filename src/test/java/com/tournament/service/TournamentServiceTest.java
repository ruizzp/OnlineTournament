package com.tournament.service;

import com.tournament.dto.TournamentCreateDto;
import com.tournament.dto.TournamentDto;
import com.tournament.model.Tournament;
import com.tournament.model.User;
import com.tournament.model.Game;
import com.tournament.model.Platform; 
import com.tournament.model.enums.UserRole;
import com.tournament.repository.TournamentRepository;
import com.tournament.repository.UserRepository;
import com.tournament.repository.GameRepository;
import com.tournament.mapper.TournamentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TournamentMapper tournamentMapper;

    @Mock
    private GameRepository gameRepository; 

    @InjectMocks
    private TournamentService tournamentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(tournamentMapper.toDto(any(Tournament.class))).thenAnswer(invocation -> {
            Tournament tournament = invocation.getArgument(0);
            return new TournamentDto(
                tournament.getPublicId(),
                tournament.getName(),
                tournament.getMaxPlayers(),
                tournament.getCurrentPlayers(),
                null, null
            );
        });
        when(tournamentMapper.toEntity(any(TournamentCreateDto.class))).thenAnswer(invocation -> {
            TournamentCreateDto dto = invocation.getArgument(0);
            Tournament tournament = new Tournament();
            tournament.setName(dto.getName());
            tournament.setMaxPlayers(dto.getMaxPlayers());
            tournament.setCurrentPlayers(0);
            tournament.setPublicId(UUID.randomUUID().toString());
            return tournament;
        });
        User mockUser = new User();
        mockUser.setRole(UserRole.ORGANIZER); 
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(mockUser));
        when(gameRepository.findByTitle(anyString())).thenReturn(Optional.of(new Game("Chess", "Strategy", Platform.PC)));
    }

    @Test
    void testGetTournamentById() {
        
        String publicId = "tournament123";
        Tournament mockTournament = new Tournament();
        mockTournament.setName("Chess Championship");
        mockTournament.setMaxPlayers(16);
        mockTournament.setPublicId(publicId); 
        when(tournamentRepository.findByPublicId(publicId)).thenReturn(Optional.of(mockTournament));

        
        TournamentDto result = tournamentService.getTournamentById(publicId);

        
        assertNotNull(result);
        assertEquals(publicId, result.getId()); 
        assertEquals("Chess Championship", result.getName());
        verify(tournamentRepository, times(1)).findByPublicId(publicId);
    }

    @Test
    void testGetTournamentById_NotFound() {
        
        String publicId = "nonexistent";
        when(tournamentRepository.findByPublicId(publicId)).thenReturn(Optional.empty());

        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            tournamentService.getTournamentById(publicId);
        });
        assertEquals("Tournament not found with id: " + publicId, exception.getMessage());
        verify(tournamentRepository, times(1)).findByPublicId(publicId);
    }

    @Test
    void testCreateTournament() {
        
        TournamentCreateDto createDto = new TournamentCreateDto(
            "Chess Championship", 16, "Chess", "organizer1"
        );

        User organizer = new User();
        organizer.setUsername("organizer1");
        organizer.setRole(UserRole.ORGANIZER);
        when(userRepository.findByUsername("organizer1")).thenReturn(Optional.of(organizer));

        Game game = new Game();
        game.setTitle("Chess");
        when(gameRepository.findByTitle("Chess")).thenReturn(Optional.of(game));

        Tournament newTournament = new Tournament();
        newTournament.setName("Chess Championship");
        newTournament.setMaxPlayers(16);
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(newTournament);

        
        TournamentDto result = tournamentService.createTournament(createDto);

        
        assertNotNull(result);
        assertEquals("Chess Championship", result.getName());
        verify(tournamentRepository, times(1)).save(any(Tournament.class));
    }
}