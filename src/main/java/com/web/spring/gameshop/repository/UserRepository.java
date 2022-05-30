package com.web.spring.gameshop.repository;

import com.web.spring.gameshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
