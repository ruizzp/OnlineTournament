package com.tournament.config;

import com.tournament.model.*;
import com.tournament.model.enums.MatchResult;
import com.tournament.model.enums.UserRole;
import com.tournament.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private GameRepository gameRepository;
    
    @Autowired
    private TournamentRepository tournamentRepository;
    
    @Autowired
    private ParticipationRepository participationRepository;
    
    @Autowired
    private MatchRepository matchRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        matchRepository.deleteAll();
        participationRepository.deleteAll();
        tournamentRepository.deleteAll();
        gameRepository.deleteAll();
        userRepository.deleteAll();
        
        User admin = new User("admin", passwordEncoder.encode("password123"), UserRole.ADMIN, 1000);
        User organizer1 = new User("organizer1", passwordEncoder.encode("password123"), UserRole.ORGANIZER, 500);
        User organizer2 = new User("organizer2", passwordEncoder.encode("password123"), UserRole.ORGANIZER, 450);
        User player1 = new User("player1", passwordEncoder.encode("password123"), UserRole.PLAYER, 850);
        User player2 = new User("player2", passwordEncoder.encode("password123"), UserRole.PLAYER, 720);
        User player3 = new User("player3", passwordEncoder.encode("password123"), UserRole.PLAYER, 680);
        User player4 = new User("player4", passwordEncoder.encode("password123"), UserRole.PLAYER, 590);
        User player5 = new User("player5", passwordEncoder.encode("password123"), UserRole.PLAYER, 420);
        
        userRepository.save(admin);
        userRepository.save(organizer1);
        userRepository.save(organizer2);
        userRepository.save(player1);
        userRepository.save(player2);
        userRepository.save(player3);
        userRepository.save(player4);
        userRepository.save(player5);
        
        Game csgo = new Game("Counter-Strike: Global Offensive", "FPS", Platform.PC);
        Game lol = new Game("League of Legends", "MOBA", Platform.PC);
        Game fifa = new Game("FIFA 24", "Sports", Platform.CONSOLE);
        Game valorant = new Game("Valorant", "FPS", Platform.PC);
        Game rocket = new Game("Rocket League", "Sports", Platform.CROSS_PLATFORM);
        
        gameRepository.save(csgo);
        gameRepository.save(lol);
        gameRepository.save(fifa);
        gameRepository.save(valorant);
        gameRepository.save(rocket);
        
        Tournament csTournament = new Tournament("CS:GO Championship", 8, csgo, organizer1);
        csTournament.setCurrentPlayers(4);
        Tournament lolTournament = new Tournament("LoL Spring Cup", 6, lol, organizer2);
        lolTournament.setCurrentPlayers(3);
        Tournament fifaTournament = new Tournament("FIFA World Cup", 4, fifa, organizer1);
        fifaTournament.setCurrentPlayers(4);
        
        tournamentRepository.save(csTournament);
        tournamentRepository.save(lolTournament);
        tournamentRepository.save(fifaTournament);
        
        Participation p1 = new Participation(csTournament, player1, LocalDateTime.now().minusDays(2), 150);
        Participation p2 = new Participation(csTournament, player2, LocalDateTime.now().minusDays(1), 120);
        Participation p3 = new Participation(csTournament, player3, LocalDateTime.now().minusHours(12), 90);
        Participation p4 = new Participation(csTournament, player4, LocalDateTime.now().minusHours(6), 80);
        
        Participation p5 = new Participation(lolTournament, player1, LocalDateTime.now().minusDays(3), 200);
        Participation p6 = new Participation(lolTournament, player3, LocalDateTime.now().minusDays(2), 180);
        Participation p7 = new Participation(lolTournament, player5, LocalDateTime.now().minusDays(1), 100);
        
        Participation p8 = new Participation(fifaTournament, player2, LocalDateTime.now().minusDays(1), 75);
        Participation p9 = new Participation(fifaTournament, player4, LocalDateTime.now().minusHours(18), 85);
        Participation p10 = new Participation(fifaTournament, player5, LocalDateTime.now().minusHours(12), 65);
        Participation p11 = new Participation(fifaTournament, admin, LocalDateTime.now().minusHours(6), 95);
        
        participationRepository.save(p1);
        participationRepository.save(p2);
        participationRepository.save(p3);
        participationRepository.save(p4);
        participationRepository.save(p5);
        participationRepository.save(p6);
        participationRepository.save(p7);
        participationRepository.save(p8);
        participationRepository.save(p9);
        participationRepository.save(p10);
        participationRepository.save(p11);
        
        Match match1 = new Match(csTournament, 1, player1, player2, MatchResult.PLAYER1_WIN);
        Match match2 = new Match(csTournament, 1, player3, player4, MatchResult.PLAYER2_WIN);
        Match match3 = new Match(csTournament, 2, player1, player4, MatchResult.PENDING);
        
        Match match4 = new Match(lolTournament, 1, player1, player3, MatchResult.PLAYER1_WIN);
        Match match5 = new Match(lolTournament, 2, player1, player5, MatchResult.DRAW);
        
        Match match6 = new Match(fifaTournament, 1, player2, player4, MatchResult.PLAYER1_WIN);
        Match match7 = new Match(fifaTournament, 1, player5, admin, MatchResult.PLAYER2_WIN);
        Match match8 = new Match(fifaTournament, 2, player2, admin, MatchResult.PENDING);
        
        matchRepository.save(match1);
        matchRepository.save(match2);
        matchRepository.save(match3);
        matchRepository.save(match4);
        matchRepository.save(match5);
        matchRepository.save(match6);
        matchRepository.save(match7);
        matchRepository.save(match8);
        
        System.out.println("Dummy data loaded successfully!");
        System.out.println("Users created: " + userRepository.count());
        System.out.println("Games created: " + gameRepository.count());
        System.out.println("Tournaments created: " + tournamentRepository.count());
        System.out.println("Participations created: " + participationRepository.count());
        System.out.println("Matches created: " + matchRepository.count());
    }
}