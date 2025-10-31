package com.tournament.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "games", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"title", "platform"})
})
public class Game {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String publicId;
    
    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String title;
    
    @NotBlank(message = "Genre is required")
    @Column(nullable = false)
    private String genre;
    
    @NotNull(message = "Platform is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;
    
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tournament> tournaments = new ArrayList<>();

    // Default constructor for JPA
    public Game() {}

    
    public Game(String title, String genre, Platform platform) {
        this.publicId = UUID.randomUUID().toString();
        this.title = title;
        this.genre = genre;
        this.platform = platform;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public List<Tournament> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<Tournament> tournaments) {
        this.tournaments = tournaments;
    }
}