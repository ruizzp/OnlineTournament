package com.tournament.model;

import com.tournament.model.enums.MatchResult;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Entity
@Table(name = "matches")
public class Match {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String publicId;
    
    @NotNull(message = "Tournament is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;
    
    @NotNull(message = "Round is required")
    @Column(nullable = false)
    private Integer round;
    
    @NotNull(message = "Player1 is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player1_id", nullable = false)
    private User player1;
    
    @NotNull(message = "Player2 is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player2_id", nullable = false)
    private User player2;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchResult result = MatchResult.PENDING;

    // Default constructor for JPA
    public Match() {}

    
    public Match(Tournament tournament, Integer round, User player1, User player2, MatchResult result) {
        this.publicId = UUID.randomUUID().toString();
        this.tournament = tournament;
        this.round = round;
        this.player1 = player1;
        this.player2 = player2;
        this.result = result != null ? result : MatchResult.PENDING;
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

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public User getPlayer1() {
        return player1;
    }

    public void setPlayer1(User player1) {
        this.player1 = player1;
    }

    public User getPlayer2() {
        return player2;
    }

    public void setPlayer2(User player2) {
        this.player2 = player2;
    }

    public MatchResult getResult() {
        return result;
    }

    public void setResult(MatchResult result) {
        this.result = result;
    }
}