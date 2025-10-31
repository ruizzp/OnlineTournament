package com.tournament.mapper;

import com.tournament.dto.ParticipationDto;
import com.tournament.model.Participation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParticipationMapper {
    
    @Autowired
    private TournamentMapper tournamentMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    public ParticipationDto toDto(Participation participation) {
        if (participation == null) {
            return null;
        }
        
        ParticipationDto dto = new ParticipationDto();
        dto.setId(participation.getPublicId());
        dto.setJoinDate(participation.getJoinDate());
        dto.setScore(participation.getScore());
        dto.setTournament(tournamentMapper.toDto(participation.getTournament()));
        dto.setPlayer(userMapper.toDto(participation.getPlayer()));
        
        return dto;
    }
}