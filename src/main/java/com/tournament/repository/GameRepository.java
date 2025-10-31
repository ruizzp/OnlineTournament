package com.tournament.repository;

import com.tournament.model.Game;
import com.tournament.model.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    
    Optional<Game> findByPublicId(String publicId);
    
    List<Game> findByGenre(String genre);
    
    List<Game> findByPlatform(Platform platform);
    
    List<Game> findByTitleContainingIgnoreCase(String title);
    
    Optional<Game> findByTitle(String title);
    
    boolean existsByTitleAndPlatform(String title, Platform platform);
}