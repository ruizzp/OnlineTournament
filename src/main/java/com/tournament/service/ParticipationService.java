package com.tournament.service;

import com.tournament.dto.ParticipationCreateDto;
import com.tournament.dto.ParticipationDto;
import com.tournament.mapper.ParticipationMapper;
import com.tournament.model.Participation;
import com.tournament.model.Tournament;
import com.tournament.model.User;
import com.tournament.model.enums.UserRole;
import com.tournament.repository.ParticipationRepository;
import com.tournament.repository.TournamentRepository;
import com.tournament.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ParticipationService {
    
    @Autowired
    private ParticipationRepository participationRepository;
    
    @Autowired
    private TournamentRepository tournamentRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TournamentService tournamentService;
    
    @Autowired
    private ParticipationMapper participationMapper;
    
    public List<ParticipationDto> getAllParticipations() {
        return participationRepository.findAll().stream()
                .map(participationMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public ParticipationDto getParticipationById(String publicId) {
        Participation participation = participationRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("Participation not found with id: " + publicId));
        return participationMapper.toDto(participation);
    }
    
    public List<ParticipationDto> getParticipationsByTournament(String tournamentPublicId) {
        Tournament tournament = tournamentRepository.findByPublicId(tournamentPublicId)
                .orElseThrow(() -> new RuntimeException("Tournament not found with id: " + tournamentPublicId));
        
        return participationRepository.findByTournament(tournament).stream()
                .map(participationMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public List<ParticipationDto> getParticipationsByPlayer(String playerPublicId) {
        User player = userRepository.findByPublicId(playerPublicId)
                .orElseThrow(() -> new RuntimeException("Player not found with id: " + playerPublicId));
        
        return participationRepository.findByPlayer(player).stream()
                .map(participationMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public List<ParticipationDto> getTournamentLeaderboard(String tournamentPublicId) {
        Tournament tournament = tournamentRepository.findByPublicId(tournamentPublicId)
                .orElseThrow(() -> new RuntimeException("Tournament not found with id: " + tournamentPublicId));
        
        return participationRepository.findByTournamentOrderByScoreDesc(tournament).stream()
                .map(participationMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public ParticipationDto createParticipation(ParticipationCreateDto participationCreateDto) {
        Tournament tournament = tournamentRepository.findByName(participationCreateDto.getTournamentName())
                .orElseThrow(() -> new RuntimeException("Tournament not found with name: " + participationCreateDto.getTournamentName()));
        
        User player = userRepository.findByUsername(participationCreateDto.getPlayerUsername())
                .orElseThrow(() -> new RuntimeException("Player not found with username: " + participationCreateDto.getPlayerUsername()));
        
        if (player.getRole() != UserRole.PLAYER && player.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only players and admins can join tournaments");
        }
        
        if (tournament.getOrganizer().getId().equals(player.getId())) {
            throw new RuntimeException("Organizer cannot join as a player in their own tournament");
        }
        
        if (tournament.getCurrentPlayers() >= tournament.getMaxPlayers()) {
            throw new RuntimeException("Tournament is already full");
        }
        
        if (participationRepository.existsByTournamentAndPlayer(tournament, player)) {
            throw new RuntimeException("Player is already participating in this tournament");
        }
        
        Participation participation = new Participation();
        participation.setPublicId(UUID.randomUUID().toString());
        participation.setTournament(tournament);
        participation.setPlayer(player);
        participation.setJoinDate(LocalDateTime.now());
        participation.setScore(participationCreateDto.getScore());
        
        Participation savedParticipation = participationRepository.save(participation);
        
        tournamentService.incrementCurrentPlayers(tournament.getPublicId());
        
        return participationMapper.toDto(savedParticipation);
    }
    
    public ParticipationDto updateParticipation(String publicId, ParticipationCreateDto participationCreateDto) {
        Participation existingParticipation = participationRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("Participation not found with id: " + publicId));
        
        existingParticipation.setScore(participationCreateDto.getScore());
        
        Participation updatedParticipation = participationRepository.save(existingParticipation);
        return participationMapper.toDto(updatedParticipation);
    }
    
    public void deleteParticipation(String publicId) {
        Participation participation = participationRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("Participation not found with id: " + publicId));
        
        tournamentService.decrementCurrentPlayers(participation.getTournament().getPublicId());
        
        participationRepository.delete(participation);
    }
    
    public ParticipationDto updateParticipationScore(String publicId, Integer score) {
        Participation participation = participationRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("Participation not found with id: " + publicId));
        
        participation.setScore(score);
        Participation updatedParticipation = participationRepository.save(participation);
        return participationMapper.toDto(updatedParticipation);
    }
}