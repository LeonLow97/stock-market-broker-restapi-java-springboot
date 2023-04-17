package com.stockmarket.stockmarketapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.stockmarket.stockmarketapi.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

  @Query(value = "SELECT COUNT(*) FROM accounts a WHERE a.email = ?1", nativeQuery = true)
  int getCountByEmail(String email);

  User findByEmail(String email);

}
