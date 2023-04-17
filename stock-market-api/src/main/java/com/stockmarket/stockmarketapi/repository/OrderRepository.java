package com.stockmarket.stockmarketapi.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.stockmarket.stockmarketapi.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

  List<Order> findAllByUserId(long userId);

  Order findByUserIdAndOrderId(long userId, long orderId);

}
