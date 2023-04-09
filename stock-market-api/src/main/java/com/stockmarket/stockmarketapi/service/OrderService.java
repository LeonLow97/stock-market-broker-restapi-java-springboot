package com.stockmarket.stockmarketapi.service;

import com.stockmarket.stockmarketapi.entity.Order;
import com.stockmarket.stockmarketapi.exception.BadRequestException;
import com.stockmarket.stockmarketapi.exception.OrderNotFilledException;

public interface OrderService {

  Order submitOrder(int userId, Order order) throws BadRequestException, OrderNotFilledException;
  
}
