package com.tournament.service;

import com.tournament.dto.UserDto;
import com.tournament.dto.UserCreateDto;
import com.tournament.mapper.UserMapper;
import com.tournament.model.User;
import com.tournament.model.enums.UserRole;
import com.tournament.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public UserDto getUserById(String publicId) {
        User user = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + publicId));
        return userMapper.toDto(user);
    }
    
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        return userMapper.toDto(user);
    }
    
    public List<UserDto> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public UserDto createUser(UserCreateDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new RuntimeException("Username already exists: " + userDto.getUsername());
        }
        
        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
    
    public UserDto updateUser(String publicId, UserCreateDto userDto) {
        User existingUser = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + publicId));
        
        if (userDto.getUsername() != null && !userDto.getUsername().equals(existingUser.getUsername())) {
            if (userRepository.existsByUsername(userDto.getUsername())) {
                throw new RuntimeException("Username already exists: " + userDto.getUsername());
            }
        }
        
        userMapper.updateEntityFromDto(userDto, existingUser);
        
        if (userDto.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDto(updatedUser);
    }
    
    public void deleteUser(String publicId) {
        User user = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + publicId));
        userRepository.delete(user);
    }
    
    public UserDto updateUserRanking(String publicId, Integer newRanking) {
        User user = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + publicId));
        
        user.setRanking(newRanking);
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }
    
    public List<UserDto> getTopRankedUsers(Integer limit) {
        return userRepository.findByRankingGreaterThanOrderByRankingDesc(0).stream()
                .limit(limit)
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
}