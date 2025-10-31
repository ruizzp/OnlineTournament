package com.tournament.repository;

import com.tournament.model.Tournament;
import com.tournament.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    
    Optional<Tournament> findByPublicId(String publicId);
    
    List<Tournament> findByOrganizer(User organizer);
    
    List<Tournament> findByCurrentPlayersLessThan(Integer maxPlayers);
    
    List<Tournament> findByGamePublicId(String gamePublicId);
    
    List<Tournament> findByNameContainingIgnoreCase(String name);
    
    Optional<Tournament> findByName(String name);
}