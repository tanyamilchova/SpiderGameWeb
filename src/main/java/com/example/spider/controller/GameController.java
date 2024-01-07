package com.example.spider.controller;

import com.example.spider.model.DTOs.*;
import com.example.spider.service.GameService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class GameController extends AbstractController{
    @Autowired
    private GameService gameService;

    @PostMapping("/games/round")
    public RespOneRoundGameDTO playOneRound(@Valid @RequestBody final OneGameDTO oneGameDTO, final HttpSession session){
        final long idLogged=loggedId(session);
        return  gameService.playOneRound(oneGameDTO, idLogged);
    }
    @GetMapping("/games")
    public List<RespOneRoundGameDTO> getAllGamesByOutcome(final HttpSession session, @RequestParam("outcome") String outcome){
        final long idLogged=loggedId(session);
        return gameService.getAllGamesByOutcome(idLogged,outcome);
    }
    @GetMapping("/games/summary")
    public ResponseSummaryDTO getSummaryForPeriod(final  HttpSession session, @RequestBody SummaryForPeriodDTO summaryForPeriodDTO){
        final long idLogged=loggedId(session);
        return gameService.getSummaryForPeriod(idLogged, summaryForPeriodDTO);
    }

}
