package com.web.spring.gameshop.repository;

import com.web.spring.gameshop.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublisherRepository extends JpaRepository<Publisher, Integer> {
    Publisher findById(int id);
    Publisher findByName(String name);
}
