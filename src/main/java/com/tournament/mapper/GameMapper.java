package com.tournament.mapper;

import com.tournament.dto.GameDto;
import com.tournament.model.Game;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {
    
    public GameDto toDto(Game game) {
        if (game == null) {
            return null;
        }
        
        GameDto dto = new GameDto();
        dto.setId(game.getPublicId());
        dto.setTitle(game.getTitle());
        dto.setGenre(game.getGenre());
        dto.setPlatform(game.getPlatform());
        
        return dto;
    }
    
    public Game toEntity(GameDto dto) {
        if (dto == null) {
            return null;
        }
        
        Game game = new Game();
        if (dto.getId() != null) {
            game.setPublicId(dto.getId());
        }
        game.setTitle(dto.getTitle());
        game.setGenre(dto.getGenre());
        game.setPlatform(dto.getPlatform());
        
        return game;
    }
    
    public void updateEntityFromDto(GameDto dto, Game game) {
        if (dto == null || game == null) {
            return;
        }
        
        if (dto.getTitle() != null) {
            game.setTitle(dto.getTitle());
        }
        if (dto.getGenre() != null) {
            game.setGenre(dto.getGenre());
        }
        if (dto.getPlatform() != null) {
            game.setPlatform(dto.getPlatform());
        }
    }
}