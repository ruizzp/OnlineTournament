package com.tournament.repository;

import com.tournament.model.Participation;
import com.tournament.model.Tournament;
import com.tournament.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    
    Optional<Participation> findByPublicId(String publicId);
    
    List<Participation> findByTournament(Tournament tournament);
    
    List<Participation> findByPlayer(User player);
    
    Optional<Participation> findByTournamentAndPlayer(Tournament tournament, User player);
    
    List<Participation> findByTournamentOrderByScoreDesc(Tournament tournament);
    
    boolean existsByTournamentAndPlayer(Tournament tournament, User player);
}