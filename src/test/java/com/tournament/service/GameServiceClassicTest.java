package com.tournament.service;

import com.tournament.controller.GameController;
import com.tournament.dto.GameDto;
import com.tournament.model.Game;
import com.tournament.model.Platform;
import com.tournament.repository.GameRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
public class GameServiceClassicTest {

    //@MockBean
    //private GameRepository gameRepository; // repository mockado

    @Autowired
    private GameService gameService;

    @Test
    void testGetGameByIdClassic() {

        GameDto mockDto = new GameDto();
        mockDto.setId("game123");
        mockDto.setTitle("Chess");
        mockDto.setPlatform(Platform.PC);
        mockDto.setGenre("Strategy");
        gameService.createGame(mockDto);

        GameDto result = gameService.getGameById("game123");


        assertNotNull(result);
        assertEquals(mockDto.getId(), result.getId());
    }


}
