package com.stockmarket.stockmarketapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.stockmarket.stockmarketapi.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
