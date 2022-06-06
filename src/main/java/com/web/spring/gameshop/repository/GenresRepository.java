package com.web.spring.gameshop.repository;

import com.web.spring.gameshop.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenresRepository extends JpaRepository<Genre, Integer> {
}
