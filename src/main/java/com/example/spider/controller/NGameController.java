package com.example.spider.controller;

import com.example.spider.model.DTOs.GameAddDTO;
import com.example.spider.model.DTOs.RespNRoundGameDTO;
import com.example.spider.service.NGameSerrvice;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NGameController extends AbstractController{
    @Autowired
    NGameSerrvice nGameSerrvice;

    @PostMapping("/games/rounds")
    public RespNRoundGameDTO playNRounds(@Valid @RequestBody final GameAddDTO gameAddDTO, final HttpSession session){
        final long idLogged=loggedId(session);
        return nGameSerrvice.playNRounds(gameAddDTO,idLogged);
    }
}
