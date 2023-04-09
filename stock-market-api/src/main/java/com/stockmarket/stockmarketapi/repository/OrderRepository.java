package com.stockmarket.stockmarketapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.stockmarket.stockmarketapi.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>  {
  
}
