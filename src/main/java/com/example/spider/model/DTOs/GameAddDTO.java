package com.example.spider.model.DTOs;

import com.example.spider.controller.Util;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameAddDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int numGames;
    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String  type;
    @Min(value = Util.STAKE, message = "Stake must be at least "+Util.STAKE)
    @Column(name = "stake")
    private int stake;
    @Column(name = "outcome")
    private String outcome;
    @Column(name = "total_win")
    private long totalWin;

    @Column(name = "sum_all_wins")
    private long sumAllWins;
    @Column(name = "sum_all_stakes")
    private long sumAllStakes;
    private long userId;
}
