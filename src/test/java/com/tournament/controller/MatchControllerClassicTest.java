package com.tournament.controller;

import com.tournament.dto.*;
import com.tournament.dto.MatchCreateDto;
import com.tournament.dto.MatchDto;
import com.tournament.dto.TournamentCreateDto;
import com.tournament.dto.UserCreateDto;
import com.tournament.dto.ParticipationCreateDto;
import com.tournament.model.enums.MatchResult;
import com.tournament.model.Platform;
import com.tournament.model.enums.UserRole;
import com.tournament.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.tournament.model.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class MatchControllerClassicTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private UserService userService;

    @Autowired
    private GameService gameService;

    @Autowired
    private ParticipationService participationService;

    @Autowired
    private MatchController matchController;

    @BeforeEach
    void setUp() {
        GameDto game = new GameDto();
        game.setTitle("Chess2");
        game.setPlatform(Platform.PC);
        game.setGenre("Strategy");
        gameService.createGame(game);

        UserCreateDto organizer = new UserCreateDto();
        organizer.setUsername("TesterPedro");
        organizer.setPassword("123");
        organizer.setRole(UserRole.ORGANIZER);
        userService.createUser(organizer);

        TournamentCreateDto tournament = new TournamentCreateDto();
        tournament.setName("Chess Championship");
        tournament.setMaxPlayers(2);
        tournament.setGameTitle("Chess2");
        tournament.setOrganizerUsername("TesterPedro");
        tournamentService.createTournament(tournament);

        ParticipationCreateDto p1 = new ParticipationCreateDto();
        p1.setTournamentName("Chess Championship");
        p1.setPlayerUsername("player1");
        p1.setScore(0);
        participationService.createParticipation(p1);

        ParticipationCreateDto p2 = new ParticipationCreateDto();
        p2.setTournamentName("Chess Championship");
        p2.setPlayerUsername("player2");
        p2.setScore(0);
        participationService.createParticipation(p2);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetMatchById() {
        MatchCreateDto matchDto = new MatchCreateDto();
        matchDto.setTournamentName("Chess Championship");
        matchDto.setPlayer1Username("player1");
        matchDto.setPlayer2Username("player2");
        matchDto.setRound(1);

        ResponseEntity<MatchDto> createdResponse = matchController.createMatch(matchDto);
        MatchDto createdMatch = createdResponse.getBody();

        // Call the controller endpoint
        ResponseEntity<MatchDto> response = matchController.getMatchById(createdMatch.getId());

        assertNotNull(response.getBody());
        assertEquals(createdMatch.getId(), response.getBody().getId());
    }
}
