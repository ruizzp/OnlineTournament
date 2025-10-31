package com.tournament.repository;

import com.tournament.model.User;
import com.tournament.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByPublicId(String publicId);
    
    Optional<User> findByUsername(String username);
    
    List<User> findByRole(UserRole role);
    
    List<User> findByRankingGreaterThanOrderByRankingDesc(Integer ranking);
    
    boolean existsByUsername(String username);
}