package com.stockmarket.stockmarketapi.service;

import java.util.List;
import com.stockmarket.stockmarketapi.entity.Order;

public interface OrderService {

  List<Order> getAllOrders(int userId);
  Order getOrder(int userId, int orderId);
  Order submitOrder(int userId, Order order);
  
}
