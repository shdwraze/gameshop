package com.web.spring.gameshop.repository;

import com.web.spring.gameshop.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Integer> {
    Game findByName(String name);
    Game findById(int id);
}
