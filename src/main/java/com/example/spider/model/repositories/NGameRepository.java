package com.example.spider.model.repositories;

import com.example.spider.model.entities.NGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NGameRepository extends JpaRepository<NGame,Integer> {
    Optional<NGame> findById(long id);
}
