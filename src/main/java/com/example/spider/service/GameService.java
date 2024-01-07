package com.example.spider.service;

import com.example.spider.model.DTOs.*;
import com.example.spider.model.entities.Game;
import com.example.spider.model.entities.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameService extends AbstractService{

    public RespOneRoundGameDTO playOneRound(OneGameDTO oneGameDTO, final long idLogged) {
        final User u = ifPresent(userRepository.findById(idLogged));
        Game game=mapper.map(oneGameDTO,Game.class);
        validateWallet(u, game.getStake(), 1);
        Game gameGenerated = generateGame(game, u);
        gameRepository.save(gameGenerated);
        return mapper.map(gameGenerated, RespOneRoundGameDTO.class);
    }

    public List<RespOneRoundGameDTO> getAllGamesByOutcome(final long idLogged, final String outcome) {
        List<Game> winGames=gameRepository.outcomeGames(idLogged, outcome);
        List<RespOneRoundGameDTO>respOneRoundGameDTOS=new ArrayList<>();
        for (Game game:winGames){
            RespOneRoundGameDTO respOneGameDTO=mapper.map(game,RespOneRoundGameDTO.class);
            respOneRoundGameDTOS.add(respOneGameDTO);
        }
        return respOneRoundGameDTOS;
    }

    public ResponseSummaryDTO getSummaryForPeriod(long idLogged, SummaryForPeriodDTO periodDTO) {

        validatePeriod(periodDTO.getStartDate(),periodDTO.getEndDate());
        List<List<Integer>>resultList= gameRepository.getSummary(idLogged,periodDTO.getStartDate(), periodDTO.getEndDate());
        ResponseSummaryDTO responseSummaryDTO= new ResponseSummaryDTO();
        responseSummaryDTO.setEndDate(periodDTO.getEndDate());
        responseSummaryDTO.setStartDate(periodDTO.getStartDate());
        if (resultList.isEmpty() || (resultList.get(0).size() < 4) || resultList.get(0).contains(null)) {
            return responseSummaryDTO;
        }

        List<Integer> innerList =  resultList.get(0);
        responseSummaryDTO.setNumGames(innerList.get(0));
        responseSummaryDTO.setStake(innerList.get(1));
        responseSummaryDTO.setTotalWin(innerList.get(2));
        responseSummaryDTO.setProfit(innerList.get(3));

        return responseSummaryDTO;
    }
}
