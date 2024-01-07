package com.example.spider.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity(name="n_games")
@Table
@Getter
@Setter
public class NGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "sum_all_stakes")
    private long sumAllStakes;
    @Column(name = "sum_all_wins")
    private long sumAllWins;
    @OneToMany(mappedBy = "nGame")
    private List<Game> games;
}
