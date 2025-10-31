package com.tournament.controller;

import com.tournament.dto.TournamentCreateDto;
import com.tournament.dto.TournamentDto;
import com.tournament.model.Game;
import com.tournament.model.Platform;
import com.tournament.model.User;
import com.tournament.model.enums.UserRole;
import com.tournament.repository.GameRepository;
import com.tournament.repository.MatchRepository;
import com.tournament.repository.TournamentRepository;
import com.tournament.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.jpa.properties.javax.persistence.validation.mode=none",
    "spring.jpa.properties.jakarta.persistence.validation.mode=none"
})
public class TournamentControllerClassicTest {

    @Autowired
    private TournamentController tournamentController;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @BeforeEach
    void initTestData() {
        try { matchRepository.deleteAllInBatch(); } catch (Exception ignored) { matchRepository.deleteAll(); }
        try { tournamentRepository.deleteAllInBatch(); } catch (Exception ignored) { tournamentRepository.deleteAll(); }
        try { userRepository.deleteAllInBatch(); } catch (Exception ignored) { userRepository.deleteAll(); }
        try { gameRepository.deleteAllInBatch(); } catch (Exception ignored) { gameRepository.deleteAll(); }

        User organizer = new User();
        organizer.setUsername("organizer1");
        organizer.setRole(UserRole.ORGANIZER);
        try {
            organizer.setPassword("P@ssw0rd1");
        } catch (Exception ignored) { }

        // best-effort: try to set email reflectively, but don't fail
        try {
            java.lang.reflect.Field f = User.class.getDeclaredField("email");
            f.setAccessible(true);
            f.set(organizer, "organizer1@example.com");
        } catch (NoSuchFieldException e1) {
            try {
                java.lang.reflect.Field f2 = User.class.getDeclaredField("emailAddress");
                f2.setAccessible(true);
                f2.set(organizer, "organizer1@example.com");
            } catch (Exception ignored) { /* no email field available, ok */ }
        } catch (Exception ignored) { /* ignore reflection errors */ }

        userRepository.saveAndFlush(organizer);

        Game chess = new Game();
        chess.setTitle("Chess2");
        chess.setGenre("Strategy");
        chess.setPlatform(Platform.PC);
        gameRepository.saveAndFlush(chess);
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    public void testCreateTournamentClassic() {
        TournamentCreateDto createDto = new TournamentCreateDto();
        createDto.setName("Chess Championship");
        createDto.setMaxPlayers(16);
        createDto.setGameTitle("Chess2");
        createDto.setOrganizerUsername("organizer1");

        ResponseEntity<TournamentDto> response = tournamentController.createTournament(createDto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Chess Championship", response.getBody().getName());
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    public void testGetAllTournamentsClassic() {
        TournamentCreateDto createDto = new TournamentCreateDto();
        createDto.setName("Mini Cup");
        createDto.setMaxPlayers(8);
        createDto.setGameTitle("Chess2");
        createDto.setOrganizerUsername("organizer1");
        tournamentController.createTournament(createDto);

        ResponseEntity<java.util.List<TournamentDto>> response = tournamentController.getAllTournaments();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() >= 1);
    }
}
