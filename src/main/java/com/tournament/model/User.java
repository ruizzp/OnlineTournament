package com.tournament.model;

import com.tournament.model.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String publicId;
    
    @NotBlank(message = "Username is required")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username may only contain letters and numbers")
    @Column(unique = true, nullable = false)
    private String username;
    
    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;
    
    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
    
    @Column(nullable = false)
    private Integer ranking = 0;
    
    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tournament> organizedTournaments = new ArrayList<>();
    
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Participation> participations = new ArrayList<>();

     // Default constructor for JPA
    public User() {}

    
    public User(String username, String password, UserRole role, Integer ranking) {
        this.publicId = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.role = role;
        this.ranking = ranking != null ? ranking : 0;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public List<Tournament> getOrganizedTournaments() {
        return organizedTournaments;
    }

    public void setOrganizedTournaments(List<Tournament> organizedTournaments) {
        this.organizedTournaments = organizedTournaments;
    }

    public List<Participation> getParticipations() {
        return participations;
    }

    public void setParticipations(List<Participation> participations) {
        this.participations = participations;
    }
}