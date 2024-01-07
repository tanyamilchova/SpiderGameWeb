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
public class RespOneRoundGameDTO {

    @Column(name = "id")
    private long id;
    @Column(name = "stake")
    private int stake;
    @Column(name = "type")
    private String  type;

    @Column(name = "outcome")
    private String outcome;
    @Column(name = "total_win")
    private long totalWin;
    @Column
    private String trace;


}
