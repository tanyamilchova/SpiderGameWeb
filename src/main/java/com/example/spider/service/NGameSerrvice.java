package com.example.spider.service;

import com.example.spider.model.DTOs.GameAddDTO;
import com.example.spider.model.DTOs.RespNRoundGameDTO;
import com.example.spider.model.entities.Game;
import com.example.spider.model.entities.NGame;
import com.example.spider.model.entities.User;
import com.example.spider.model.exceptions.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class NGameSerrvice extends AbstractService{
    @Transactional
    public RespNRoundGameDTO playNRounds(GameAddDTO gameAddDTO, final long idLogged) {
        User user=ifPresent(userRepository.findById(idLogged));
        int numGames=gameAddDTO.getNumGames();
        if(numGames<2) {
            throw new BadRequestException("The number of games must be greater than 1");
        }
        validateWallet(user, gameAddDTO.getStake(), numGames);
        NGame nGame=mapper.map(gameAddDTO,NGame.class);
        nGameRepository.save(nGame);
        NGame nGameSaved=ifPresent(nGameRepository.findById(nGame.getId()));

        long sumAllStakes=0;
        long sumAllWins=0;

        for (int i = 0; i < numGames; i++) {
            Game game=mapper.map(gameAddDTO,Game.class);
            Game gameGenerated= generateGame(game, user);
            game.setNGame(nGameSaved);
            sumAllStakes+=gameGenerated.getStake();
            sumAllWins+=gameGenerated.getTotalWin();
            gameRepository.save(gameGenerated);
        }
        nGameSaved.setSumAllStakes( sumAllStakes);
        nGameSaved.setSumAllWins(sumAllWins);
        nGameRepository.save(nGame);
        RespNRoundGameDTO respNRoundGameDTO=mapper.map(nGameSaved,RespNRoundGameDTO.class);
        respNRoundGameDTO.setRatioWinsStakes((double) sumAllWins / sumAllStakes);

        return respNRoundGameDTO;
    }

}
