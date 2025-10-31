package com.tournament.mapper;

import com.tournament.dto.TournamentDto;
import com.tournament.dto.TournamentCreateDto;
import com.tournament.model.Tournament;
import com.tournament.model.Game;
import com.tournament.model.User;
import com.tournament.model.enums.UserRole;
import com.tournament.model.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TournamentMapper {
    
    @Autowired
    private GameMapper gameMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    public TournamentDto toDto(Tournament tournament) {
        if (tournament == null) {
            return null;
        }
        
        System.out.println("Mapping Tournament to DTO: publicId=" + tournament.getPublicId());
        
        TournamentDto dto = new TournamentDto();
        dto.setId(tournament.getPublicId());
        dto.setName(tournament.getName());
        dto.setMaxPlayers(tournament.getMaxPlayers());
        dto.setCurrentPlayers(tournament.getCurrentPlayers());
        dto.setGame(gameMapper.toDto(tournament.getGame()));
        dto.setOrganizer(userMapper.toDto(tournament.getOrganizer()));
        
        return dto;
    }
    
    public Tournament toEntity(TournamentCreateDto dto) {
        if (dto == null) {
            return null;
        }

        Tournament tournament = new Tournament();
        tournament.setName(dto.getName());
        tournament.setMaxPlayers(dto.getMaxPlayers());
        tournament.setCurrentPlayers(0); 
        tournament.setPublicId(UUID.randomUUID().toString()); 

        if (dto.getGameTitle() != null) {
            Game game = new Game();
            game.setTitle(dto.getGameTitle());
            tournament.setGame(game);
        }

        if (dto.getOrganizerUsername() != null) {
            User organizer = new User();
            organizer.setUsername(dto.getOrganizerUsername());
            tournament.setOrganizer(organizer);
        }

        return tournament;
    }
    
    public void updateEntityFromDto(TournamentCreateDto dto, Tournament tournament) {
        if (dto == null || tournament == null) {
            return;
        }
        
        if (dto.getName() != null) {
            tournament.setName(dto.getName());
        }
        if (dto.getMaxPlayers() != null) {
            tournament.setMaxPlayers(dto.getMaxPlayers());
        }
    }
}