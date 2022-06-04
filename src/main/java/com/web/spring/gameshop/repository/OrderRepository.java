package com.web.spring.gameshop.repository;

import com.web.spring.gameshop.entity.Order;
import com.web.spring.gameshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    void deleteByUserAndId(User user, int id);
}
