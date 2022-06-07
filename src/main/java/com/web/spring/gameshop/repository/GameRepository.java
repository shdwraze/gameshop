package com.web.spring.gameshop.repository;

import com.web.spring.gameshop.entity.Game;
import com.web.spring.gameshop.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Integer> {
    Game findByName(String name);
    Game findById(int id);

    List<Game> findAllByGenres(Genre genre);
}
