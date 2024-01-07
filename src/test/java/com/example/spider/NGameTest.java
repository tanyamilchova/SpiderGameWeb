package com.example.spider;

import com.example.spider.model.DTOs.GameAddDTO;
import com.example.spider.model.DTOs.RespNRoundGameDTO;
import com.example.spider.model.entities.Game;
import com.example.spider.model.entities.NGame;
import com.example.spider.model.entities.User;
import com.example.spider.model.exceptions.BadRequestException;
import com.example.spider.model.repositories.GameRepository;
import com.example.spider.model.repositories.NGameRepository;
import com.example.spider.model.repositories.UserRepository;
import com.example.spider.service.AbstractService;
import com.example.spider.service.NGameSerrvice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NGameTest {

    @InjectMocks
    private NGameSerrvice nGameService;
    @Mock
    private AbstractService abstractService;

    @Mock
    private NGameRepository nGameRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper mapper;

    @Test
    public void testPlayNRounds() {
        GameAddDTO gameAddDTO = new GameAddDTO();
        gameAddDTO.setNumGames(2);
        gameAddDTO.setStake(11);
        RespNRoundGameDTO expectedResponse=new RespNRoundGameDTO();

        long userId = 1;
        long nGameId = 1;
        long gameId = 1;
        User user = new User();
        user.setId(userId);
        user.setWallet(100);
        NGame nGame = new NGame();
        nGame.setId(nGameId);
        Game game = new Game();
        game.setUser(user);
        game.setId(gameId);
        game.setNGame(nGame);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mapper.map(gameAddDTO, NGame.class)).thenReturn(nGame);
        when(nGameRepository.save(any(NGame.class))).thenReturn(nGame);
        when(nGameRepository.findById(nGameId)).thenReturn(Optional.of(nGame));
        when(mapper.map(gameAddDTO, Game.class)).thenReturn(game);
        when(mapper.map(nGame, RespNRoundGameDTO.class)).thenReturn(expectedResponse);

        RespNRoundGameDTO actualResponse = nGameService.playNRounds(gameAddDTO, userId);

        verify(nGameRepository, times(2)).save(any(NGame.class));
        verify(gameRepository, times(2)).save(any(Game.class));

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testPlayNRoundsInvalidNumGames() {
        GameAddDTO gameAddDTO = new GameAddDTO();
        gameAddDTO.setNumGames(1);
        gameAddDTO.setStake(11);

        long userId = 1;
        User user = new User();
        user.setId(userId);
        user.setWallet(100);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> nGameService.playNRounds(gameAddDTO, userId));

        assertEquals("The number of games must be greater than 1", exception.getMessage());
    }

    @Test
    public void testPlayNRoundsNotEnoughMoney() {
        GameAddDTO gameAddDTO = new GameAddDTO();
        gameAddDTO.setNumGames(2);
        gameAddDTO.setStake(11);

        long userId = 1;
        User user = new User();
        user.setId(userId);
        user.setWallet(10);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> nGameService.playNRounds(gameAddDTO, userId));

        assertEquals("Not enough money in your wallet", exception.getMessage());
    }


}

