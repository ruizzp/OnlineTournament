package com.tournament.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tournaments")
public class Tournament {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String publicId;
    
    @NotBlank(message = "Tournament name is required")
    @Column(nullable = false)
    private String name;
    
    @NotNull(message = "Max players is required")
    @Min(value = 2, message = "Tournament must have at least 2 players")
    @Column(nullable = false)
    private Integer maxPlayers;
    
    @Column(nullable = false)
    private Integer currentPlayers = 0;
    
    @NotNull(message = "Game is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
    
    @NotNull(message = "Organizer is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;
    
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Participation> participations = new ArrayList<>();
    
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Match> matches = new ArrayList<>();

     // Default constructor for JPA
    public Tournament() {}

    
    public Tournament(String name, Integer maxPlayers, Game game, User organizer) {
        this.publicId = UUID.randomUUID().toString();
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.currentPlayers = 0;
        this.game = game;
        this.organizer = organizer;
    }

    @PrePersist
    private void generatePublicId() {
        if (this.publicId == null) {
            this.publicId = UUID.randomUUID().toString();
        }
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Integer getCurrentPlayers() {
        return currentPlayers;
    }

    public void setCurrentPlayers(Integer currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public List<Participation> getParticipations() {
        return participations;
    }

    public void setParticipations(List<Participation> participations) {
        this.participations = participations;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
}