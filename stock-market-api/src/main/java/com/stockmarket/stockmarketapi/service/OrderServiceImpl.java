package com.stockmarket.stockmarketapi.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stockmarket.stockmarketapi.DTOs.OrderSubmitDTO;
import com.stockmarket.stockmarketapi.entity.Order;
import com.stockmarket.stockmarketapi.entity.Portfolio;
import com.stockmarket.stockmarketapi.entity.User;
import com.stockmarket.stockmarketapi.exception.BadRequestException;
import com.stockmarket.stockmarketapi.exception.InternalServerErrorException;
import com.stockmarket.stockmarketapi.exception.OrderNotFilledException;
import com.stockmarket.stockmarketapi.exception.ResourceNotFoundException;
import com.stockmarket.stockmarketapi.repository.OrderRepository;
import com.stockmarket.stockmarketapi.repository.PortfolioRepository;
import com.stockmarket.stockmarketapi.repository.UserRepository;
import yahoofinance.Stock;
import yahoofinance.quotes.stock.StockQuote;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

  @Autowired
  OrderRepository orderRepository;

  @Autowired
  PortfolioRepository portfolioRepository;

  @Autowired
  UserRepository userRepository;

  enum ORDER_TYPE {
    BUY, SELL
  }

  @Override
  public List<Order> getAllOrders(int userId) {
    List<Order> orders = orderRepository.findAllByUserId(Long.valueOf(userId));
    if (orders.isEmpty()) {
      throw new ResourceNotFoundException("No orders were found.");
    }
    return orders;
  }

  @Override
  public Order getOrder(int userId, int orderId) {
    Order order =
        orderRepository.findByUserIdAndOrderId(Long.valueOf(userId), Long.valueOf(orderId));
    if (order == null) {
      throw new ResourceNotFoundException("No order was found.");
    }
    return order;
  }

  @Override
  public Order submitOrder(int userId, OrderSubmitDTO orderSubmitDTO) {
    try {
      // Trim Fields
      orderSubmitDTO.setOrderType(orderSubmitDTO.getOrderType().trim());

      if (orderSubmitDTO.getStockTicker().isBlank() || orderSubmitDTO.getOrderType().isBlank()) {
        throw new BadRequestException("Fill in all fields.");
      }
      if (orderSubmitDTO.getStockTicker().length() > 20) {
        throw new BadRequestException("Stock Ticker has a maximum of 20 characters.");
      }
      if (orderSubmitDTO.getOrderType().length() > 4) {
        throw new BadRequestException("Order Type has a maximum of 4 characters.");
      }

      ORDER_TYPE type = ORDER_TYPE.valueOf(orderSubmitDTO.getOrderType().toUpperCase());
      if (orderSubmitDTO.getNoOfShares() < 0) {
        throw new BadRequestException("Number of shares cannot be negative.");
      }
      if (orderSubmitDTO.getCost() < 0) {
        throw new BadRequestException("Price cannot be negative.");
      }

      // Retrieve user's current portfolio details
      Portfolio dbPortfolio = portfolioRepository.findByUserIdAndStockTicker(Long.valueOf(userId),
          orderSubmitDTO.getStockTicker().trim());
      if (dbPortfolio == null && type == ORDER_TYPE.SELL) {
        throw new ResourceNotFoundException(
            "Unable to sell stock as it does not exist in user's portfolio.");
      }
      if (type == ORDER_TYPE.SELL) {
        if (orderSubmitDTO.getNoOfShares() > dbPortfolio.getNoOfShares()) {
          throw new OrderNotFilledException("Insufficient no. of shares.");
        }
      }

      // Check if price is between the market's high and low within the day.
      Stock stock = new Stock(orderSubmitDTO.getStockTicker());
      StockQuote stockQuote = stock.getQuote(true);
      BigDecimal orderPrice = new BigDecimal(orderSubmitDTO.getCost());

      // Check if user's account has sufficient funds
      Optional<User> dbUser = userRepository.findById(Long.valueOf(userId));
      if (!dbUser.isPresent()) {
        throw new ResourceNotFoundException("User not found.");
      }
      Double dbBalance = dbUser.map(u -> u.getBalance())
          .orElseThrow(() -> new BadRequestException("User not found."));

      // Check if user has sufficient funds to purchase stocks and specified number of
      // shares &
      // price
      Double cost = orderSubmitDTO.getCost() * orderSubmitDTO.getNoOfShares();
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
          orderSubmitDTO.setCost(stockQuote.getDayLow().doubleValue());
          dbBalance =
              dbBalance + (orderSubmitDTO.getNoOfShares() * stockQuote.getDayLow().doubleValue());
        }
      }

      // Specified price is more than the intra-day high
      if (orderPrice.compareTo(stockQuote.getDayHigh()) > 0) {
        if (type == ORDER_TYPE.BUY) {
          orderSubmitDTO.setCost(stockQuote.getDayHigh().doubleValue());
          dbBalance =
              dbBalance - (orderSubmitDTO.getNoOfShares() * stockQuote.getDayHigh().doubleValue());
        } else if (type == ORDER_TYPE.SELL) {
          throw new OrderNotFilledException(
              "Order not filled as price is more than highest ask price on " + LocalDateTime.now());
        }
      }

      // Specified price is within the intra-day price
      if (orderPrice.compareTo(stockQuote.getDayLow()) >= 0
          && orderPrice.compareTo(stockQuote.getDayHigh()) <= 0) {
        if (type == ORDER_TYPE.BUY) {
          dbBalance = dbBalance - cost;
        } else if (type == ORDER_TYPE.SELL) {
          dbBalance = dbBalance + cost;
        }
      }

      Order order = new Order((long) userId, orderSubmitDTO.getStockTicker(),
          orderSubmitDTO.getOrderType(), orderSubmitDTO.getNoOfShares(), orderSubmitDTO.getCost());
      order = orderRepository.save(order);
      User updatedUser = dbUser.get();
      updatedUser.setBalance(dbBalance);
      userRepository.save(updatedUser);

      // Create or update user's portfolio (BUY)
      if (type == ORDER_TYPE.BUY) {
        if (dbPortfolio == null) {
          // Create a new stock in the portfolio
          dbPortfolio = new Portfolio();
          dbPortfolio.setUserId(Long.valueOf(userId));
          dbPortfolio.setStockName(stock.getName());
          dbPortfolio.setStockTicker(stock.getQuote().getSymbol());
          dbPortfolio.setPrice(stock.getQuote().getPrice().doubleValue());
          dbPortfolio.setNoOfShares(order.getNoOfShares());
          dbPortfolio.setCost(order.getCost());
          dbPortfolio = calculateStockPortfolioData(dbPortfolio, order);
          portfolioRepository.save(dbPortfolio);
        } else {
          dbPortfolio.setPrice(stock.getQuote().getPrice().doubleValue());
          dbPortfolio.setNoOfShares(dbPortfolio.getNoOfShares() + order.getNoOfShares());
          dbPortfolio = calculateStockPortfolioData(dbPortfolio, order);
          portfolioRepository.save(dbPortfolio);
        }
      }

      // Update user's portfolio (SELL)
      if (type == ORDER_TYPE.SELL) {
        int noOfSharesLeft = dbPortfolio.getNoOfShares() - order.getNoOfShares();
        if (noOfSharesLeft == 0) {
          portfolioRepository.delete(dbPortfolio);
        } else {
          dbPortfolio.setPrice(stock.getQuote().getPrice().doubleValue());
          dbPortfolio.setNoOfShares(dbPortfolio.getNoOfShares() - order.getNoOfShares());
          dbPortfolio = calculateStockPortfolioData(dbPortfolio, order);
          portfolioRepository.save(dbPortfolio);
        }
      }

      return order;
    } catch (IOException e) {
      throw new InternalServerErrorException("Error connecting with Yahoo Finance API");
    } catch (NullPointerException e) {
      throw new NullPointerException("NullPointerException: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Order Type must be BUY or SELL.");
    }
  }

  private Portfolio calculateStockPortfolioData(Portfolio portfolio, Order order) {
    // Update Cost of stock in portfolio
    Double marketValueCurrentStocks = portfolio.getNoOfShares() * portfolio.getCost();
    Double marketValueUpdatedStocks = order.getNoOfShares() * order.getCost();
    int totalNoOfShares = portfolio.getNoOfShares() + order.getNoOfShares();
    Double updatedCost = (marketValueCurrentStocks + marketValueUpdatedStocks) / totalNoOfShares;
    portfolio.setCost(updatedCost);

    // Update PNL in %
    Double cost = portfolio.getCost();
    Double price = portfolio.getPrice();
    Double updatedPNLPercentage = ((price - cost) / cost) * 100;
    portfolio.setPNLInPercentage(updatedPNLPercentage);

    Double updatePNLDollars = (price - cost) * portfolio.getNoOfShares();
    portfolio.setPNLInDollars(updatePNLDollars);

    return portfolio;
  }

}
