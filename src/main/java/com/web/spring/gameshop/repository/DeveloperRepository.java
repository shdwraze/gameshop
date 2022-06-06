package com.web.spring.gameshop.repository;

import com.web.spring.gameshop.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeveloperRepository extends JpaRepository<Developer, Integer> {
    Developer findById(int id);
    Developer findByName(String name);
}
