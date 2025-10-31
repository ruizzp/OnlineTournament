package com.tournament.mapper;

import com.tournament.dto.MatchDto;
import com.tournament.model.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MatchMapper {
    
    @Autowired
    private TournamentMapper tournamentMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    public MatchDto toDto(Match match) {
        if (match == null) {
            return null;
        }
        
        MatchDto dto = new MatchDto();
        dto.setId(match.getPublicId());
        dto.setRound(match.getRound());
        dto.setResult(match.getResult());
        dto.setTournament(tournamentMapper.toDto(match.getTournament()));
        dto.setPlayer1(userMapper.toDto(match.getPlayer1()));
        dto.setPlayer2(userMapper.toDto(match.getPlayer2()));
        
        return dto;
    }
    
}