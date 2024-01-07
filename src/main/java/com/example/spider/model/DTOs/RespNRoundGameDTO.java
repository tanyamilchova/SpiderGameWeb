package com.example.spider.model.DTOs;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RespNRoundGameDTO {

    @Column(name = "id")
    private long id;
    @Column(name = "sum_all_wins")
    private long allWins;
    @Column(name = "sum_all_stakes")
    private long allStakes;
    private double ratioWinsStakes;


}
