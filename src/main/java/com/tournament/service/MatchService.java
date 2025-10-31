package com.tournament.service;

import com.tournament.dto.MatchCreateDto;
import com.tournament.dto.MatchDto;
import com.tournament.mapper.MatchMapper;
import com.tournament.model.Match;
import com.tournament.model.Tournament;
import com.tournament.model.User;
import com.tournament.model.enums.MatchResult;
import com.tournament.repository.MatchRepository;
import com.tournament.repository.ParticipationRepository;
import com.tournament.repository.TournamentRepository;
import com.tournament.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class MatchService {
    
    @Autowired
    private MatchRepository matchRepository;
    
    @Autowired
    private TournamentRepository tournamentRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ParticipationRepository participationRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private MatchMapper matchMapper;
    
    public List<MatchDto> getAllMatches() {
        return matchRepository.findAll().stream()
                .map(matchMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public MatchDto getMatchById(String publicId) {
        Match match = matchRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + publicId));
        return matchMapper.toDto(match);
    }
    
    public List<MatchDto> getMatchesByTournament(String tournamentPublicId) {
        Tournament tournament = tournamentRepository.findByPublicId(tournamentPublicId)
                .orElseThrow(() -> new RuntimeException("Tournament not found with id: " + tournamentPublicId));
        
        return matchRepository.findByTournamentOrderByRoundAsc(tournament).stream()
                .map(matchMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public List<MatchDto> getMatchesByTournamentAndRound(String tournamentPublicId, Integer round) {
        Tournament tournament = tournamentRepository.findByPublicId(tournamentPublicId)
                .orElseThrow(() -> new RuntimeException("Tournament not found with id: " + tournamentPublicId));
        
        return matchRepository.findByTournamentAndRound(tournament, round).stream()
                .map(matchMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public List<MatchDto> getMatchesByPlayer(String playerPublicId) {
        User player = userRepository.findByPublicId(playerPublicId)
                .orElseThrow(() -> new RuntimeException("Player not found with id: " + playerPublicId));
        
        return matchRepository.findByPlayer1OrPlayer2(player, player).stream()
                .map(matchMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public MatchDto createMatch(MatchCreateDto matchCreateDto) {
        Tournament tournament = tournamentRepository.findByName(matchCreateDto.getTournamentName())
                .orElseThrow(() -> new RuntimeException("Tournament not found with name: " + matchCreateDto.getTournamentName()));
        
        User player1 = userRepository.findByUsername(matchCreateDto.getPlayer1Username())
                .orElseThrow(() -> new RuntimeException("Player1 not found with username: " + matchCreateDto.getPlayer1Username()));
        
        User player2 = userRepository.findByUsername(matchCreateDto.getPlayer2Username())
                .orElseThrow(() -> new RuntimeException("Player2 not found with username: " + matchCreateDto.getPlayer2Username()));
        
        boolean player1Registered = participationRepository.existsByTournamentAndPlayer(tournament, player1);
        boolean player2Registered = participationRepository.existsByTournamentAndPlayer(tournament, player2);
        
        if (!player1Registered) {
            throw new RuntimeException("Player1 is not registered in this tournament");
        }
        
        if (!player2Registered) {
            throw new RuntimeException("Player2 is not registered in this tournament");
        }
        
        Match match = new Match();
        match.setPublicId(UUID.randomUUID().toString());
        match.setTournament(tournament);
        match.setPlayer1(player1);
        match.setPlayer2(player2);
        match.setRound(matchCreateDto.getRound());
        match.setResult(matchCreateDto.getResult() != null ? matchCreateDto.getResult() : MatchResult.PENDING);
        
        Match savedMatch = matchRepository.save(match);
        return matchMapper.toDto(savedMatch);
    }
    
    public MatchDto updateMatch(String publicId, MatchCreateDto matchCreateDto) {
        Match existingMatch = matchRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + publicId));
        
        MatchResult oldResult = existingMatch.getResult();
        
        existingMatch.setRound(matchCreateDto.getRound());
        if (matchCreateDto.getResult() != null) {
            existingMatch.setResult(matchCreateDto.getResult());
        }
        
        Match updatedMatch = matchRepository.save(existingMatch);
        
        if (matchCreateDto.getResult() != null && !matchCreateDto.getResult().equals(oldResult)) {
            updatePlayerRankings(updatedMatch);
        }
        
        return matchMapper.toDto(updatedMatch);
    }
    
    public void deleteMatch(String publicId) {
        Match match = matchRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + publicId));
        matchRepository.delete(match);
    }
    
    public MatchDto updateMatchResult(String publicId, MatchResult result) {
        Match match = matchRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + publicId));
        
        match.setResult(result);
        Match updatedMatch = matchRepository.save(match);
        
        updatePlayerRankings(updatedMatch);
        
        return matchMapper.toDto(updatedMatch);
    }
    
    private void updatePlayerRankings(Match match) {
        if (match.getResult() == MatchResult.PENDING || match.getResult() == MatchResult.DRAW) {
            return;
        }
        
        User winner = null;
        User loser = null;
        
        if (match.getResult() == MatchResult.PLAYER1_WIN) {
            winner = match.getPlayer1();
            loser = match.getPlayer2();
        } else if (match.getResult() == MatchResult.PLAYER2_WIN) {
            winner = match.getPlayer2();
            loser = match.getPlayer1();
        }
        
        if (winner != null && loser != null) {
            userService.updateUserRanking(winner.getPublicId(), winner.getRanking() + 10);
            int newLoserRanking = Math.max(0, loser.getRanking() - 5); 
            userService.updateUserRanking(loser.getPublicId(), newLoserRanking);
        }
    }
}