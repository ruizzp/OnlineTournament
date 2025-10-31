package com.tournament.repository;

import com.tournament.model.Match;
import com.tournament.model.Tournament;
import com.tournament.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    
    Optional<Match> findByPublicId(String publicId);
    
    List<Match> findByTournament(Tournament tournament);
    
    List<Match> findByTournamentAndRound(Tournament tournament, Integer round);
    
    List<Match> findByPlayer1OrPlayer2(User player1, User player2);
    
    List<Match> findByTournamentOrderByRoundAsc(Tournament tournament);
}