package com.tournament.service;

import com.tournament.dto.UserCreateDto;
import com.tournament.dto.UserDto;
import com.tournament.mapper.UserMapper;
import com.tournament.model.User;
import com.tournament.model.enums.UserRole;
import com.tournament.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(userMapper.toDto(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            return new UserDto(user.getPublicId(), user.getUsername(), user.getRole(), user.getRanking());
        });
        when(userMapper.toEntity(any(UserCreateDto.class))).thenAnswer(invocation -> {
            UserCreateDto dto = invocation.getArgument(0);
            User user = new User();
            user.setUsername(dto.getUsername());
            user.setRole(dto.getRole());
            user.setRanking(dto.getRanking());
            return user;
        });
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("encodedPassword");
    }

    @Test
    void testGetUserByUsername() {
        
        String username = "testuser";
        User mockUser = new User();
        mockUser.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        
        UserDto result = userService.getUserByUsername(username);

        
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testGetUserByUsername_NotFound() {
        
        String username = "nonexistentuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserByUsername(username);
        });
        assertEquals("User not found with username: " + username, exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testCreateUser() {
        
        UserCreateDto newUserDto = new UserCreateDto("newuser", "password123", UserRole.PLAYER, 0);
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setPassword("password123");
        newUser.setRole(UserRole.PLAYER);
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        when(userRepository.existsByUsername("newuser")).thenReturn(false);

        
        UserDto result = userService.createUser(newUserDto);

        
        assertNotNull(result);
        assertEquals("newuser", result.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_DuplicateUsername() {
        
        UserCreateDto newUserDto = new UserCreateDto("existinguser", "password123", UserRole.PLAYER, 0);
        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(newUserDto);
        });
        assertEquals("Username already exists: existinguser", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateUser_Success() {
        
        UserCreateDto dto = new UserCreateDto("testUser", "password", UserRole.PLAYER, 100);
        User user = userMapper.toEntity(dto);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.existsByUsername("testUser")).thenReturn(false);

        
        UserDto result = userService.createUser(dto);

        
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        assertEquals(UserRole.PLAYER, result.getRole());
        assertEquals(100, result.getRanking());
        verify(userRepository, times(1)).save(any(User.class));
    }
}