package com.example.spider;

import com.example.spider.controller.Util;
import com.example.spider.model.DTOs.OneGameDTO;
import com.example.spider.model.DTOs.RespOneRoundGameDTO;
import com.example.spider.model.entities.Game;
import com.example.spider.model.entities.User;
import com.example.spider.model.exceptions.BadRequestException;
import com.example.spider.model.exceptions.NotFoundException;
import com.example.spider.model.repositories.GameRepository;
import com.example.spider.model.repositories.UserRepository;
import com.example.spider.service.AbstractService;
import com.example.spider.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class GameTest {
    @InjectMocks
    private GameService gameService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private AbstractService abstractService;
    @Mock
    private ModelMapper mapper;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPlayOneRound() {
        OneGameDTO oneGameDTO = Util.getOneGameDTO();
        RespOneRoundGameDTO expectedResponse = new RespOneRoundGameDTO();
        long userId = 1;
        long gameId = 1;
        User user = new User();
        user.setId(userId);
        Game game = new Game();
        game.setUser(user);
        game.setId(gameId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mapper.map(oneGameDTO, Game.class)).thenReturn(game);
        when(mapper.map(game, RespOneRoundGameDTO.class)).thenReturn(expectedResponse);

        RespOneRoundGameDTO actualResponse = gameService.playOneRound(oneGameDTO, userId);

        assertEquals(expectedResponse, actualResponse);

        verify(gameRepository, times(1)).save(game);
    }

    @Test
    public void testValidateNotEnoughMoney() {
        OneGameDTO oneGameDTO = Util.getOneGameDTO();
        long userId = 1;
        long gameId = 1;
        User user = new User();
        user.setId(userId);
        user.setWallet(0);
        Game game = new Game();
        game.setUser(user);
        game.setId(gameId);
        game.setStake(oneGameDTO.getStake());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mapper.map(oneGameDTO, Game.class)).thenReturn(game);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> gameService.playOneRound(oneGameDTO, userId));
        assertEquals("Not enough money in your wallet", exception.getMessage());
        verify(gameRepository, never()).save(any());
    }
    @Test
    public void testPersonNotPresent() {
        OneGameDTO oneGameDTO = Util.getOneGameDTO();
        long userId = 1000;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> gameService.playOneRound(oneGameDTO,userId));
    }

    @Test
    public void filterGamesByOutcome(){
        String outcome=Util.OUTCOME;
        User user=new User();
        long userId=1;
        user.setId(userId);
        Game game1=new Game();
        game1.setUser(user);
        game1.setOutcome(Util.OUTCOME);

        Game game2=new Game();
        game2.setUser(user);
        game2.setOutcome(Util.OUTCOME);

        List<Game> outcomeList=new ArrayList<>();
        outcomeList.add(game1);
        outcomeList.add(game2);
        List<RespOneRoundGameDTO>expected= Arrays.asList(
                mapper.map(game1,RespOneRoundGameDTO.class),
                mapper.map(game2,RespOneRoundGameDTO.class)
        );

        when(gameRepository.outcomeGames(userId,outcome)).thenReturn(outcomeList);
        List<RespOneRoundGameDTO>result=gameService.getAllGamesByOutcome(userId,outcome);

        assertNotNull(result);
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i),result.get(i));
        }
    }
    //   public ResponseSummaryDTO getSummaryForPeriod(long idLogged, SummaryForPeriodDTO periodDTO) {
    //
    //        validatePeriod(periodDTO.getStartDate(),periodDTO.getEndDate());
    //        List<List<Integer>>resultList= gameRepository.getSummary(idLogged,periodDTO.getStartDate(), periodDTO.getEndDate());
    //        ResponseSummaryDTO responseSummaryDTO= new ResponseSummaryDTO();
    //        responseSummaryDTO.setEndDate(periodDTO.getEndDate());
    //        responseSummaryDTO.setStartDate(periodDTO.getStartDate());
    //        if (resultList.isEmpty() || (resultList.get(0).size() < 4) || resultList.get(0).contains(null)) {
    //            return responseSummaryDTO;
    //        }
    //
    //        List<Integer> innerList =  resultList.get(0);
    //        responseSummaryDTO.setNumGames(innerList.get(0));
    //        responseSummaryDTO.setStake(innerList.get(1));
    //        responseSummaryDTO.setTotalWin(innerList.get(2));
    //        responseSummaryDTO.setProfit(innerList.get(3));
    //
    //        return responseSummaryDTO;
    //    }
//    @Test
//    void summaryPeriodSuccessful(){
//        User user=new User();
//        long idLogged=1;
//        user.setId(idLogged);
//
//        SummaryForPeriodDTO periodDTO=new SummaryForPeriodDTO();
//        periodDTO.setStartDate(LocalDate.now().minusDays(2));
//        periodDTO.setEndDate(LocalDate.now());
//
//        List<List<Integer>>resultList=new ArrayList<>();
//        List<Integer>res=new ArrayList<>();
//        res.add(90);
//        res.add(100);
//        res.add(200);
//        res.add(100);
//        resultList.add(res);
//        System.out.println(resultList);
//        System.out.println(res);
//
//        ResponseSummaryDTO responseSummaryDTO= new ResponseSummaryDTO();
//        //        responseSummaryDTO.setEndDate(periodDTO.getEndDate());
//        //        responseSummaryDTO.setStartDate(periodDTO.getStartDate());
//
//        assertTrue(periodDTO.getStartDate().isBefore(periodDTO.getEndDate()));
//        assertNotNull(gameRepository.getSummary(idLogged,periodDTO.getStartDate(), periodDTO.getEndDate()));
//        when(gameRepository.getSummary(idLogged,periodDTO.getStartDate(), periodDTO.getEndDate())).thenReturn(resultList);
//
//        ResponseSummaryDTO responseSummaryDTO=new ResponseSummaryDTO();
//        responseSummaryDTO.setStartDate(LocalDate.now().minusDays(2));
//        responseSummaryDTO.setEndDate(LocalDate.now());
//        responseSummaryDTO.setNumGames(res.get(0));
//        responseSummaryDTO.setStake(res.get(1));
//        responseSummaryDTO.setTotalWin(res.get(2));
//        responseSummaryDTO.setProfit(res.get(3));
//
//        ResponseSummaryDTO result=gameService.getSummaryForPeriod(idLogged,periodDTO);
//
//       // when(gameService.getSummaryForPeriod(idLogged,periodDTO)).thenReturn(responseSummaryDTO);
//        assertEquals(responseSummaryDTO,result);
//
//   }
}

