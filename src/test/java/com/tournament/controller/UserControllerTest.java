package com.tournament.controller;

import com.tournament.dto.UserCreateDto;
import com.tournament.dto.UserDto;
import com.tournament.model.enums.UserRole;
import com.tournament.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        
        UserDto mockUser = new UserDto();
        mockUser.setId("user123");
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(mockUser));

        
        ResponseEntity<List<UserDto>> response = userController.getAllUsers();

        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("user123", response.getBody().get(0).getId());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserById() {
        
        UserDto mockUser = new UserDto();
        mockUser.setId("user123");
        when(userService.getUserById("user123")).thenReturn(mockUser);

        
        ResponseEntity<UserDto> response = userController.getUserById("user123");

        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("user123", response.getBody().getId());
    }

    @Test
    void testCreateUser() {
        
        UserCreateDto createDto = new UserCreateDto();
        createDto.setUsername("newUser");
        createDto.setRole(UserRole.PLAYER);

        UserDto mockUser = new UserDto();
        mockUser.setId("user123");
        mockUser.setUsername("newUser");
        when(userService.createUser(createDto)).thenReturn(mockUser);

        
        ResponseEntity<UserDto> response = userController.createUser(createDto);

        
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("user123", response.getBody().getId());
        assertEquals("newUser", response.getBody().getUsername());
        verify(userService, times(1)).createUser(createDto);
    }
}