package com.example.spider.controller;

import com.example.spider.model.DTOs.OneGameDTO;

public class Util {
    public static final int STAKE = 11;
    public static final String LOGGED="LOGGED";
    public static final String LOGGED_ID="LOGGED_ID";
    public static final String WIN = "Win";
    public static final String LOSS = "Loss";
    public static final int PROFIT_PER_STEPS = 1;
    public static final String EMAIL = "test@example.com";
    public static final String PASS = "123456Sbb#";
    public static final String NAME = "Name";
    public static final String OUTCOME = "Outcome";
    public static OneGameDTO getOneGameDTO() {
        OneGameDTO oneGameDTO = new OneGameDTO();
        oneGameDTO.setName("SpiderOneRoundGame");
        oneGameDTO.setType("1 round");
        oneGameDTO.setStake(11);
        return oneGameDTO;
    }


}
