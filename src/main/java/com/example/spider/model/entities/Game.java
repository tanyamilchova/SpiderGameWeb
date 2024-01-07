package com.example.spider.model.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name="games")
@Table
@Getter
@Setter
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String  type;
    @Column(name = "stake")
    private int stake;
    @Column(name = "total_win")
    private long totalWin;
    @Column(name = "outcome")
    private String outcome;
    @Column(name = "date_time_game")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime dateTimeGame;
    @Column(name = "trace")
    private String trace;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "n_game_id")
    private NGame nGame;
}
