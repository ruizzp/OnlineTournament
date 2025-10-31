package com.tournament.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "participations")
public class Participation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String publicId;
    
    @NotNull(message = "Tournament is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;
    
    @NotNull(message = "Player is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private User player;
    
    @Column(nullable = false)
    private LocalDateTime joinDate;
    
    @Column(nullable = false)
    private Integer score = 0;

    // Default constructor for JPA
    public Participation() {}

    
    public Participation(Tournament tournament, User player, LocalDateTime joinDate, Integer score) {
        this.publicId = UUID.randomUUID().toString();
        this.tournament = tournament;
        this.player = player;
        this.joinDate = joinDate != null ? joinDate : LocalDateTime.now();
        this.score = score != null ? score : 0;
    }

    @PrePersist
    private void generatePublicId() {
        if (this.publicId == null) {
            this.publicId = UUID.randomUUID().toString();
        }
        if (this.joinDate == null) {
            this.joinDate = LocalDateTime.now();
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

    public User getPlayer() {
        return player;
    }

    public void setPlayer(User player) {
        this.player = player;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}