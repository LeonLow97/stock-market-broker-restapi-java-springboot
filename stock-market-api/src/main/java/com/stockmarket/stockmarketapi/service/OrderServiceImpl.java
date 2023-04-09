package com.stockmarket.stockmarketapi.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stockmarket.stockmarketapi.entity.Order;
import com.stockmarket.stockmarketapi.entity.User;
import com.stockmarket.stockmarketapi.exception.BadRequestException;
import com.stockmarket.stockmarketapi.exception.OrderNotFilledException;
import com.stockmarket.stockmarketapi.repository.OrderRepository;
import com.stockmarket.stockmarketapi.repository.UserRepository;
import yahoofinance.Stock;
import yahoofinance.quotes.stock.StockQuote;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

  @Autowired
  OrderRepository orderRepository;

  @Autowired
  UserRepository userRepository;

  enum ORDER_TYPE {
    BUY, SELL
  }

  @Override
  public List<Order> getAllOrders(int userId) {
    return orderRepository.findAllByUserId(Long.valueOf(userId));
  }

  @Override
  public Order submitOrder(int userId, Order order)
      throws BadRequestException, OrderNotFilledException {
    // Trim Fields
    order.setOrderType(order.getOrderType().trim());

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

    ORDER_TYPE type = ORDER_TYPE.valueOf(order.getOrderType().toUpperCase());
    if (type == null) {
      throw new BadRequestException("Order Type must be BUY or SELL.");
    }

    if (order.getNoOfShares() < 0) {
      throw new BadRequestException("Number of shares cannot be negative.");
    }
    if (order.getPrice() < 0) {
      throw new BadRequestException("Price cannot be negative.");
    }

    order.setUserId(Long.valueOf(userId));

    try {
      // Check if price is between the market's high and low within the day.
      Stock stock = new Stock(order.getStockTicker());
      StockQuote stockQuote = stock.getQuote(true);
      BigDecimal orderPrice = new BigDecimal(order.getPrice());

      // Check if user's account has sufficient funds
      Optional<User> dbUser = userRepository.findById(Long.valueOf(userId));
      Double dbBalance = dbUser.map(u -> u.getBalance())
          .orElseThrow(() -> new BadRequestException("User not found"));

      // Check if user has sufficient funds to purchase stocks and specified number of shares &
      // price
      Double cost = order.getPrice() * order.getNoOfShares();
      if (dbBalance < cost && type == ORDER_TYPE.BUY) {
        throw new OrderNotFilledException(
            "Balance is insufficient to cover the total cost of stocks.");
      }

      // Specified price is less than the intra-day low
      if (orderPrice.compareTo(stockQuote.getDayLow()) < 0) {
        if (type == ORDER_TYPE.BUY) {
          throw new OrderNotFilledException(
              "Order not filled as price is less than lowest bid price on " + LocalDateTime.now());
        } else if (type == ORDER_TYPE.SELL) {
          order.setPrice(stockQuote.getDayLow().doubleValue());
          dbBalance = dbBalance + (order.getNoOfShares() * stockQuote.getDayLow().doubleValue());
        }
      }

      // Specified price is more than the intra-day high
      if (orderPrice.compareTo(stockQuote.getDayHigh()) > 0) {
        if (type == ORDER_TYPE.BUY) {
          order.setPrice(stockQuote.getDayHigh().doubleValue());
          dbBalance = dbBalance - (order.getNoOfShares() * stockQuote.getDayHigh().doubleValue());
        } else if (type == ORDER_TYPE.SELL) {
          throw new OrderNotFilledException(
              "Order not filled as price is more than highest ask price on " + LocalDateTime.now());
        }
      }

      // Specified price is within the intra-day price
      if (orderPrice.compareTo(stockQuote.getDayLow()) >= 0
          || orderPrice.compareTo(stockQuote.getDayHigh()) <= 0) {
        if (type == ORDER_TYPE.BUY) {
          dbBalance = dbBalance - cost;
        } else if (type == ORDER_TYPE.SELL) {
          dbBalance = dbBalance + cost;
        }
      }

      order = orderRepository.save(order);
      User updatedUser = dbUser.get();
      updatedUser.setBalance(dbBalance);
      userRepository.save(updatedUser);

      return order;
    } catch (IOException e) {
      System.out.println("Failed to connect to YahooFinanceAPI: " + e.getMessage());
    }
    return null;
  }

}
