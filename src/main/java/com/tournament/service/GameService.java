package com.tournament.service;

import com.tournament.dto.GameDto;
import com.tournament.mapper.GameMapper;
import com.tournament.model.Game;
import com.tournament.model.Platform;
import com.tournament.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GameService {
    
    @Autowired
    private GameRepository gameRepository;
    
    @Autowired
    private GameMapper gameMapper;
    
    public List<GameDto> getAllGames() {
        return gameRepository.findAll().stream()
                .map(gameMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public GameDto getGameById(String publicId) {
        Game game = gameRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("Game not found with id: " + publicId));
        return gameMapper.toDto(game);
    }
    
    public List<GameDto> getGamesByGenre(String genre) {
        return gameRepository.findByGenre(genre).stream()
                .map(gameMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public List<GameDto> getGamesByPlatform(Platform platform) {
        return gameRepository.findByPlatform(platform).stream()
                .map(gameMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public List<GameDto> searchGamesByTitle(String title) {
        return gameRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(gameMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public GameDto createGame(GameDto gameDto) {
        if (gameRepository.existsByTitleAndPlatform(gameDto.getTitle(), gameDto.getPlatform())) {
            throw new RuntimeException("Game with title '" + gameDto.getTitle() + "' already exists on platform '" + gameDto.getPlatform() + "'");
        }
        
        Game game = gameMapper.toEntity(gameDto);
        Game savedGame = gameRepository.save(game);
        return gameMapper.toDto(savedGame);
    }
    
    public GameDto updateGame(String publicId, GameDto gameDto) {
        Game existingGame = gameRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("Game not found with id: " + publicId));
        
        if (!existingGame.getTitle().equals(gameDto.getTitle()) || 
            !existingGame.getPlatform().equals(gameDto.getPlatform())) {
            if (gameRepository.existsByTitleAndPlatform(gameDto.getTitle(), gameDto.getPlatform())) {
                throw new RuntimeException("Game with title '" + gameDto.getTitle() + "' already exists on platform '" + gameDto.getPlatform() + "'");
            }
        }
        
        gameMapper.updateEntityFromDto(gameDto, existingGame);
        Game updatedGame = gameRepository.save(existingGame);
        return gameMapper.toDto(updatedGame);
    }
    
    public void deleteGame(String publicId) {
        Game game = gameRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("Game not found with id: " + publicId));
        gameRepository.delete(game);
    }
}