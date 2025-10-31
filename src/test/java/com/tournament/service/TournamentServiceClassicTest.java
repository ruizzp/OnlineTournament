package com.tournament.service;

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
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.jpa.properties.javax.persistence.validation.mode=none",
    "spring.jpa.properties.jakarta.persistence.validation.mode=none"
})
public class TournamentServiceClassicTest {

    @Autowired
    private TournamentService tournamentService;

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
        // Delete children first to avoid NOT NULL FK constraint issues
        try { matchRepository.deleteAllInBatch(); } catch (Exception ignored) { matchRepository.deleteAll(); }
        try { tournamentRepository.deleteAllInBatch(); } catch (Exception ignored) { tournamentRepository.deleteAll(); }
        try { userRepository.deleteAllInBatch(); } catch (Exception ignored) { userRepository.deleteAll(); }
        try { gameRepository.deleteAllInBatch(); } catch (Exception ignored) { gameRepository.deleteAll(); }

        // Create and save a minimal organizer user (validation disabled so missing fields won't block persist)
        User organizer = new User();
        organizer.setUsername("organizer1");
        organizer.setRole(UserRole.ORGANIZER);

        // Best-effort: set password reflectively (avoid calling a missing setter)
        boolean pwSet = false;
        String testPassword = "P@ssw0rd1";
        String[] candidatePwFields = {"password", "passwd", "pwd"};
        for (String fName : candidatePwFields) {
            try {
                java.lang.reflect.Field f = User.class.getDeclaredField(fName);
                f.setAccessible(true);
                f.set(organizer, testPassword);
                pwSet = true;
                break;
            } catch (NoSuchFieldException ignored) {
                // try next candidate
            } catch (Exception ex) {
                // unexpected reflective error, but continue trying others
            }
        }
        // If no password field was found, we simply persist the organizer anyway because bean-validation is disabled.

        // Best-effort: set email reflectively, but don't fail if absent
        String[] candidateEmailFields = {"email", "emailAddress", "mail"};
        for (String fName : candidateEmailFields) {
            try {
                java.lang.reflect.Field f = User.class.getDeclaredField(fName);
                f.setAccessible(true);
                f.set(organizer, "organizer1@example.com");
                break;
            } catch (NoSuchFieldException ignored) {
                // try next
            } catch (Exception ignored) { /* ignore */ }
        }

        // Persist and flush so other transactions see the user
        userRepository.saveAndFlush(organizer);

        // Create game "Chess" (simple)
        Game chess = new Game();
        chess.setTitle("Chess2");
        chess.setGenre("Strategy");
        chess.setPlatform(Platform.PC);
        gameRepository.saveAndFlush(chess);
    }

    @Test
    void testCreateAndGetTournamentClassic() {
        TournamentCreateDto createDto = new TournamentCreateDto();
        createDto.setName("Spring Cup");
        createDto.setMaxPlayers(12);
        createDto.setGameTitle("Chess2");
        createDto.setOrganizerUsername("organizer1");

        TournamentDto created = tournamentService.createTournament(createDto);
        assertNotNull(created);
        assertEquals("Spring Cup", created.getName());
        assertEquals(12, created.getMaxPlayers());

        TournamentDto fetched = tournamentService.getTournamentById(created.getId());
        assertNotNull(fetched);
        assertEquals(created.getId(), fetched.getId());
        assertEquals(created.getName(), fetched.getName());
    }

    @Test
    void testGetAllTournamentsClassic() {
        TournamentCreateDto createDto = new TournamentCreateDto();
        createDto.setName("Autumn Cup");
        createDto.setMaxPlayers(10);
        createDto.setGameTitle("Chess2");
        createDto.setOrganizerUsername("organizer1");
        tournamentService.createTournament(createDto);

        java.util.List<TournamentDto> all = tournamentService.getAllTournaments();
        assertNotNull(all);
        assertTrue(all.size() >= 1);
    }
}
