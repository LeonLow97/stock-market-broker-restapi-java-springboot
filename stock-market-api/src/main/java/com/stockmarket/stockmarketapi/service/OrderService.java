package com.stockmarket.stockmarketapi.service;

import java.util.List;
import com.stockmarket.stockmarketapi.entity.Order;
import com.stockmarket.stockmarketapi.exception.BadRequestException;
import com.stockmarket.stockmarketapi.exception.OrderNotFilledException;

public interface OrderService {

  List<Order> getAllOrders(int userId);
  Order submitOrder(int userId, Order order) throws BadRequestException, OrderNotFilledException;
  
}
