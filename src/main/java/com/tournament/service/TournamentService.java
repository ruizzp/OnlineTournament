package com.tournament.service;

import com.tournament.dto.TournamentDto;
import com.tournament.dto.TournamentCreateDto;
import com.tournament.mapper.TournamentMapper;
import com.tournament.model.Game;
import com.tournament.model.Tournament;
import com.tournament.model.User;
import com.tournament.model.enums.UserRole;
import com.tournament.repository.GameRepository;
import com.tournament.repository.TournamentRepository;
import com.tournament.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TournamentService {
    
    @Autowired
    private TournamentRepository tournamentRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private GameRepository gameRepository;
    
    @Autowired
    private TournamentMapper tournamentMapper;
    
    public List<TournamentDto> getAllTournaments() {
        return tournamentRepository.findAll().stream()
                .map(tournamentMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public TournamentDto getTournamentById(String publicId) {
        Tournament tournament = tournamentRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("Tournament not found with id: " + publicId));
        return tournamentMapper.toDto(tournament);
    }
    
    public List<TournamentDto> getTournamentsByOrganizer(String organizerPublicId) {
        User organizer = userRepository.findByPublicId(organizerPublicId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + organizerPublicId));
        
        return tournamentRepository.findByOrganizer(organizer).stream()
                .map(tournamentMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public List<TournamentDto> getAvailableTournaments() {
        return tournamentRepository.findAll().stream()
                .filter(t -> t.getCurrentPlayers() < t.getMaxPlayers())
                .map(tournamentMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public TournamentDto createTournament(TournamentCreateDto tournamentCreateDto) {
        User organizer = userRepository.findByUsername(tournamentCreateDto.getOrganizerUsername())
                .orElseThrow(() -> new RuntimeException("Organizer not found with username: " + tournamentCreateDto.getOrganizerUsername()));
        
        if (organizer.getRole() != UserRole.ORGANIZER && organizer.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only organizers and admins can create tournaments");
        }
        
        Game game = gameRepository.findByTitle(tournamentCreateDto.getGameTitle())
                .orElseThrow(() -> new RuntimeException("Game not found with title: " + tournamentCreateDto.getGameTitle()));
        
        Tournament tournament = tournamentMapper.toEntity(tournamentCreateDto);
        tournament.setOrganizer(organizer);
        tournament.setGame(game);
        
        Tournament savedTournament = tournamentRepository.save(tournament);
        return tournamentMapper.toDto(savedTournament);
    }
    
    public TournamentDto updateTournament(String publicId, TournamentCreateDto tournamentCreateDto) {
        Tournament existingTournament = tournamentRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("Tournament not found with id: " + publicId));
        
        if (tournamentCreateDto.getGameTitle() != null) {
            Game game = gameRepository.findByTitle(tournamentCreateDto.getGameTitle())
                    .orElseThrow(() -> new RuntimeException("Game not found with title: " + tournamentCreateDto.getGameTitle()));
            existingTournament.setGame(game);
        }
        
        if (tournamentCreateDto.getOrganizerUsername() != null) {
            User organizer = userRepository.findByUsername(tournamentCreateDto.getOrganizerUsername())
                    .orElseThrow(() -> new RuntimeException("Organizer not found with username: " + tournamentCreateDto.getOrganizerUsername()));
            
            if (organizer.getRole() != UserRole.ORGANIZER && organizer.getRole() != UserRole.ADMIN) {
                throw new RuntimeException("Only organizers and admins can organize tournaments");
            }
            existingTournament.setOrganizer(organizer);
        }
        
        tournamentMapper.updateEntityFromDto(tournamentCreateDto, existingTournament);
        Tournament updatedTournament = tournamentRepository.save(existingTournament);
        return tournamentMapper.toDto(updatedTournament);
    }
    
    public void deleteTournament(String publicId) {
        Tournament tournament = tournamentRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("Tournament not found with id: " + publicId));
        tournamentRepository.delete(tournament);
    }
    
    public TournamentDto incrementCurrentPlayers(String publicId) {
        Tournament tournament = tournamentRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("Tournament not found with id: " + publicId));
        
        if (tournament.getCurrentPlayers() >= tournament.getMaxPlayers()) {
            throw new RuntimeException("Tournament is already full");
        }
        
        tournament.setCurrentPlayers(tournament.getCurrentPlayers() + 1);
        Tournament updatedTournament = tournamentRepository.save(tournament);
        return tournamentMapper.toDto(updatedTournament);
    }
    
    public TournamentDto decrementCurrentPlayers(String publicId) {
        Tournament tournament = tournamentRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("Tournament not found with id: " + publicId));
        
        if (tournament.getCurrentPlayers() > 0) {
            tournament.setCurrentPlayers(tournament.getCurrentPlayers() - 1);
            Tournament updatedTournament = tournamentRepository.save(tournament);
            return tournamentMapper.toDto(updatedTournament);
        }
        
        return tournamentMapper.toDto(tournament);
    }
}