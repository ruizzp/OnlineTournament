package com.tournament.mapper;

import com.tournament.dto.UserDto;
import com.tournament.dto.UserCreateDto;
import com.tournament.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    
    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        
        UserDto dto = new UserDto();
        dto.setId(user.getPublicId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        dto.setRanking(user.getRanking());
        
        return dto;
    }
    
    public User toEntity(UserCreateDto dto) {
        if (dto == null) {
            return null;
        }
        
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        user.setRanking(dto.getRanking() != null ? dto.getRanking() : 0);
        
        return user;
    }
    
    public void updateEntityFromDto(UserCreateDto dto, User user) {
        if (dto == null || user == null) {
            return;
        }
        
        if (dto.getUsername() != null) {
            user.setUsername(dto.getUsername());
        }
        if (dto.getPassword() != null) {
            user.setPassword(dto.getPassword());
        }
        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }
        if (dto.getRanking() != null) {
            user.setRanking(dto.getRanking());
        }
    }
}