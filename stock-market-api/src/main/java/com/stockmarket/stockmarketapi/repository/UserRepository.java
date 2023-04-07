package com.stockmarket.stockmarketapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.stockmarket.stockmarketapi.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
