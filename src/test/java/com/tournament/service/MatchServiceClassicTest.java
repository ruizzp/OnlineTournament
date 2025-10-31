package com.tournament.service;

import com.tournament.dto.MatchCreateDto;
import com.tournament.dto.MatchDto;
import com.tournament.dto.GameDto;
import com.tournament.dto.TournamentCreateDto;
import com.tournament.dto.UserCreateDto;
import com.tournament.dto.ParticipationCreateDto;
import com.tournament.model.Tournament;
import com.tournament.model.User;
import com.tournament.model.enums.UserRole;
import com.tournament.model.Platform;
import com.tournament.repository.TournamentRepository;
import com.tournament.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MatchServiceClassicTest {

    @Autowired
    private MatchService matchService;

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private UserService userService;

    @Autowired
    private GameService gameService;

    @Autowired
    private ParticipationService participationService;

    @BeforeEach
    void setUp() {
        GameDto game = new GameDto();
        game.setTitle("Chess3");
        game.setPlatform(Platform.PC);
        game.setGenre("Strategy");
        gameService.createGame(game);

        UserCreateDto tester = new UserCreateDto();
        tester.setUsername("TesterPedro");
        tester.setPassword("123");
        tester.setRole(UserRole.ORGANIZER);
        userService.createUser(tester);

        TournamentCreateDto tournament = new TournamentCreateDto();
        tournament.setName("Chess Championship");
        tournament.setMaxPlayers(2);
        tournament.setGameTitle("Chess3");
        tournament.setOrganizerUsername("TesterPedro");
        tournamentService.createTournament(tournament);

        /*UserCreateDto player1 = new UserCreateDto();
        player1.setUsername("player1");
        player1.setPassword("123");
        player1.setRole(UserRole.PLAYER);
        userService.createUser(player1);

        UserCreateDto player2 = new UserCreateDto();
        player2.setUsername("player2");
        player2.setPassword("123");
        player2.setRole(UserRole.PLAYER);
        userService.createUser(player2);*/

        ParticipationCreateDto participate = new ParticipationCreateDto();
        participate.setTournamentName("Chess Championship");
        participate.setPlayerUsername("player1");
        participate.setScore(0);
        participationService.createParticipation(participate);
        participate.setPlayerUsername("player2");
        participationService.createParticipation(participate);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateMatchClassic() {
        // Given
        MatchCreateDto createDto = new MatchCreateDto();
        createDto.setTournamentName("Chess Championship");
        createDto.setPlayer1Username("player1");
        createDto.setPlayer2Username("player2");
        createDto.setRound(1);

        // When
        MatchDto result = matchService.createMatch(createDto);

        System.out.println("MatchDto: " + result);
        System.out.println("Match ID: " + result.getId());

        // Then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Chess Championship", result.getTournament().getName());
        assertEquals(1, result.getRound());
        assertEquals("player1", result.getPlayer1().getUsername());
        assertEquals("player2", result.getPlayer2().getUsername());
    }
}
