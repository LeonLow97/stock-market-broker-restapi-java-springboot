package com.stockmarket.stockmarketapi.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stockmarket.stockmarketapi.entity.Order;
import com.stockmarket.stockmarketapi.exception.BadRequestException;
import com.stockmarket.stockmarketapi.exception.OrderNotFilledException;
import com.stockmarket.stockmarketapi.repository.OrderRepository;
import yahoofinance.Stock;
import yahoofinance.quotes.stock.StockQuote;

@Service
public class OrderServiceImpl implements OrderService {

  @Autowired
  OrderRepository orderRepository;

  enum Type {
    BUY, SELL
  }

  @Override
  public Order submitOrder(int userId, Order order)
      throws BadRequestException, OrderNotFilledException {
    if (order.getStockTicker() == null || order.getStockTicker().isBlank()
        || order.getOrderType() == null || order.getOrderType().isBlank()) {
      throw new BadRequestException("Fill in all fields.");
    }

    if (order.getStockTicker().length() > 20) {
      throw new BadRequestException("Stock Ticker has a maximum of 20 characters.");
    }

    if (order.getOrderType().length() > 4) {
      throw new BadRequestException("Order Type has a maximum of 4 characters.");
    }

    if (Type.valueOf(order.getOrderType().toUpperCase()) == null) {
      throw new BadRequestException("Order Type must be BUY or SELL.");
    }

    if (order.getNoOfShares() < 0) {
      throw new BadRequestException("Number of shares cannot be negative.");
    }

    order.setUserId(Long.valueOf(userId));

    try {
      // Check if price is between the market's high and low within the day.
      Stock stock = new Stock(order.getStockTicker());
      StockQuote stockQuote = stock.getQuote(true);
      BigDecimal orderPrice = new BigDecimal(order.getPrice());

      if (orderPrice.compareTo(stockQuote.getDayLow()) <= 0) {
        throw new OrderNotFilledException("Order not filled as price did not satisfy ask price on " + LocalDateTime.now());
      }

      

      if (orderPrice.compareTo(stockQuote.getDayHigh()) >= 0) {
        order.setPrice(stockQuote.getDayHigh().doubleValue());
        order = orderRepository.save(order);
      }

    } catch (IOException e) {
      System.out.println("Failed to connect to YahooFinanceAPI: " + e.getMessage());
    }
    return order;
  }

}
