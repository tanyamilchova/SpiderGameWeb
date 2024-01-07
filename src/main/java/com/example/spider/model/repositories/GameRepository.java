package com.example.spider.model.repositories;

import com.example.spider.model.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface GameRepository extends JpaRepository<Game,Integer> {

    @Query(value =" SELECT*FROM games WHERE user_id= :userId AND outcome= :outcome ",nativeQuery = true)
    List<Game> outcomeGames(@Param("userId") long userId, @Param("outcome") String outcome);
    @Query(value =
            "SELECT COUNT(id) AS numGames, SUM(stake) AS stake, SUM(total_win) AS totalWin, " +
                    "SUM(total_win) - SUM(stake) AS profit " +
                    "FROM games " +
                    "WHERE user_id = :userId AND date_time_game >= :startDate AND date_time_game < :endDate",
            nativeQuery = true)
    List<List<Integer>> getSummary(@Param("userId") long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
