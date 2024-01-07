package com.example.spider.model.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseSummaryDTO {
    private int numGames;
    private int stake;
    private int totalWin;
    private int profit;
    private LocalDate startDate;
    private LocalDate endDate;

}
