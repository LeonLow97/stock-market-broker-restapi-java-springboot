package com.stockmarket.stockmarketapi.servicetests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.mockito.junit.jupiter.MockitoExtension;
import com.stockmarket.stockmarketapi.DTOs.OrderSubmitDTO;
import com.stockmarket.stockmarketapi.entity.Order;
import com.stockmarket.stockmarketapi.entity.Portfolio;
import com.stockmarket.stockmarketapi.entity.User;
import com.stockmarket.stockmarketapi.exception.BadRequestException;
import com.stockmarket.stockmarketapi.exception.OrderNotFilledException;
import com.stockmarket.stockmarketapi.exception.ResourceNotFoundException;
import com.stockmarket.stockmarketapi.repository.OrderRepository;
import com.stockmarket.stockmarketapi.repository.PortfolioRepository;
import com.stockmarket.stockmarketapi.repository.UserRepository;
import com.stockmarket.stockmarketapi.service.OrderServiceImpl;
import yahoofinance.quotes.stock.StockQuote;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {

    private List<Order> orders;
    private Order order;
    private User user;
    private Portfolio portfolio;

    @Mock
    OrderRepository orderRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    PortfolioRepository portfolioRepository;

    @Mock
    StockQuote stockQuote;

    @InjectMocks
    OrderServiceImpl orderServiceImpl;

    @BeforeEach
    void setup() throws Exception {
        orders = Arrays.asList(
                new Order(1L, "BABA", "SELL", 60, 124.3),
                new Order(1L, "TSLA", "BUY", 10, 133.4),
                new Order(1L, "AAPL", "SELL", 50, 163.1));
        order = new Order(1L, "JPM", "BUY", 120, 128.7);
        user = new User("leonlow", "Password0!", "leonlow@service,com", 1000.0, 1);
        portfolio = new Portfolio(1L, "Google", "GOOGL", 20, 122.17, 108.23, 0.0, 0.0);
    }

    @Test
    public void Test_GetAllOrdersSuccess() {
        // Arrange
        when(orderRepository.findAllByUserId(1L)).thenReturn(orders);

        // Act
        List<Order> dbOrders = orderServiceImpl.getAllOrders(1);

        // Assert
        assertEquals(orders, dbOrders);
        verify(orderRepository, times(1)).findAllByUserId(1L);
    }

    @Test
    public void Test_GetAllOrdersWhenNoOrdersFound() {
        // Arrange
        List<Order> emptyOrder = new ArrayList<>();
        when(orderRepository.findAllByUserId(1L)).thenReturn(emptyOrder);

        // Act
        Throwable exception = assertThrows(ResourceNotFoundException.class, () -> {
            orderServiceImpl.getAllOrders(1);
        });

        // Assert
        assertEquals("No orders were found.", exception.getMessage());
        verify(orderRepository, times(1)).findAllByUserId(1L);
    }

    @Test
    public void Test_GetOrderSuccess() {
        // Arrange
        when(orderRepository.findByUserIdAndOrderId(1L, 1L)).thenReturn(order);

        // Act
        Order dbOrder = orderServiceImpl.getOrder(1, 1);

        // Assert
        assertEquals(dbOrder, order);
        verify(orderRepository, times(1)).findByUserIdAndOrderId(1L, 1L);
    }

    @Test
    public void Test_GetOrderWhenOrderNotFound() {
        // Arrange
        when(orderRepository.findByUserIdAndOrderId(1L, 1L)).thenReturn(null);

        // Act 
        Throwable exception = assertThrows(ResourceNotFoundException.class, () -> {
            orderServiceImpl.getOrder(1, 1);
        });

        // Assert
        assertEquals("No order was found.", exception.getMessage());
        verify(orderRepository, times(1)).findByUserIdAndOrderId(1L, 1L);
    }

    @Test
    public void Test_SubmitOrderSuccess() {
        // // Arrange
        // Portfolio portfolio = new Portfolio(1L, "Google", "GOOGL", 20, 122.17, 108.23, 0.0, 0.0);
        // Order order = new Order(1L, "GOOGL", "SELL", 20, 100.0);
        // User dbUser = new User("leonlow", "Password0!", "leonlow@service.com", 1000.0);
        // when(userRepository.findById(1L)).thenReturn(Optional.of(dbUser));
        // when(portfolioRepository.findByUserIdAndStockTicker(1L, "GOOGL")).thenReturn(portfolio);

        // // Act
        // Order dbOrder = orderServiceImpl.submitOrder(1, order);


    }

    @Test
    public void Test_SubmitOrderWhenStockTickerIsBlank() {
        // Arrange
        OrderSubmitDTO blankStockTickerOrder = new OrderSubmitDTO("", "BUY", 10, 0.0);

        // Act
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            orderServiceImpl.submitOrder(1, blankStockTickerOrder);
        });

        // Assert
        assertEquals("Fill in all fields.", exception.getMessage());
    }

    @Test
    public void Test_SubmitOrderWhenOrderTypeIsBlank() {
        // Arrange
        OrderSubmitDTO blankOrderType = new OrderSubmitDTO("GOOGL", "", 10, 0.0);

        // Act
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            orderServiceImpl.submitOrder(1, blankOrderType);
        });

        // Assert
        assertEquals("Fill in all fields.", exception.getMessage());
    }

    @Test
    public void Test_SubmitOrderWhenInvalidStockTickerLength() {
        // Arrange
        OrderSubmitDTO invalidOrder = new OrderSubmitDTO("thisstocktickerhasalengthmorethan20characters", "BUY", 10, 0.0);

        // Act
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            orderServiceImpl.submitOrder(1, invalidOrder);
        });

        // Assert
        assertEquals("Stock Ticker has a maximum of 20 characters.", exception.getMessage());
    }

    @Test
    public void Test_SubmitOrderWhenInvalidOrderTypeLength() {
        // Arrange
        OrderSubmitDTO invalidOrder = new OrderSubmitDTO("GOOGL", "BUYSELL", 10, 0.0);

        // Act
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            orderServiceImpl.submitOrder(1, invalidOrder);
        });

        // Assert
        assertEquals("Order Type has a maximum of 4 characters.", exception.getMessage());
    }

    @Test
    public void Test_SubmitOrderWhenInvalidOrderType() {
        // Arrange
        OrderSubmitDTO invalidOrder = new OrderSubmitDTO("GOOGL", "BUYS", 10, 0.0);

        // Act
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            orderServiceImpl.submitOrder(1, invalidOrder);
        });

        // Assert
        assertEquals("Order Type must be BUY or SELL.", exception.getMessage());
    }

    @Test
    public void Test_SubmitOrderWhenNoOfSharesNegative() {
        // Arrange
        OrderSubmitDTO invalidOrder = new OrderSubmitDTO("GOOGL", "BUY", -10, 0.0);

        // Act
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            orderServiceImpl.submitOrder(1, invalidOrder);
        });

        // Assert
        assertEquals("Number of shares cannot be negative.", exception.getMessage());
    }

    @Test
    public void Test_SubmitOrderWhenCostNegative() {
        // Arrange
        OrderSubmitDTO invalidOrder = new OrderSubmitDTO("GOOGL", "BUY", 10, -100.0);

        // Act
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            orderServiceImpl.submitOrder(1, invalidOrder);
        });

        // Assert
        assertEquals("Price cannot be negative.", exception.getMessage());
    }

    @Test
    public void Test_SubmitOrderWhenStockNotFoundInPortfolio() {
        // Arrange
        OrderSubmitDTO invalidOrder = new OrderSubmitDTO("GOOGL", "SELL", 10, 0.0);
        when(portfolioRepository.findByUserIdAndStockTicker(1L, "GOOGL")).thenReturn(null);

        // Act
        Throwable exception = assertThrows(ResourceNotFoundException.class, () -> {
            orderServiceImpl.submitOrder(1, invalidOrder);
        });

        // Assert
        assertEquals("Unable to sell stock as it does not exist in user's portfolio.", exception.getMessage());
    }

    @Test
    public void Test_SubmitOrderWhenToSellInvalidNoOfShares() {
        // Arrange
        Portfolio dbPortfolio = new Portfolio(1L, "Google", "GOOGL", 20, 122.17, 108.23, 0.0, 0.0);
        OrderSubmitDTO invalidOrder = new OrderSubmitDTO("GOOGL", "SELL", 21, 0.0);
        when(portfolioRepository.findByUserIdAndStockTicker(1L, "GOOGL")).thenReturn(dbPortfolio);

        // Act
        Throwable exception = assertThrows(OrderNotFilledException.class, () -> {
            orderServiceImpl.submitOrder(1, invalidOrder);
        });

        // Assert
        assertEquals("Insufficient no. of shares.", exception.getMessage());
    }

    @Test
    public void Test_SubmitOrderWhenUserNotFoundById() {
        // Arrange
        Portfolio dbPortfolio = new Portfolio(1L, "Google", "GOOGL", 20, 122.17, 108.23, 0.0, 0.0);
        OrderSubmitDTO invalidOrder = new OrderSubmitDTO("GOOGL", "SELL", 20, 100.0);
        User dbUser = new User("leonlow", "Password0!", "leonlow@service.com", 1000.0, 1);
        when(portfolioRepository.findByUserIdAndStockTicker(1L, "GOOGL")).thenReturn(dbPortfolio);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Throwable exception = assertThrows(ResourceNotFoundException.class, () -> {
            orderServiceImpl.submitOrder(1, invalidOrder);
        });

        // Assert
        assertEquals("User not found.", exception.getMessage());
    }

    @Test
    public void Test_SubmitOrderWhenBalanceIsInsufficient() {
        // Arrange
        OrderSubmitDTO invalidOrder = new OrderSubmitDTO("BABA", "BUY", 20, 100.0);
        User dbUser = new User("leonlow", "Password0!", "leonlow@service.com", 1000.0, 1);
        when(userRepository.findById(1L)).thenReturn(Optional.of(dbUser));

        // Act
        Throwable exception = assertThrows(OrderNotFilledException.class, () -> {
            orderServiceImpl.submitOrder(1, invalidOrder);
        });

        // Assert
        assertEquals("Balance is insufficient to cover the total cost of stocks.", exception.getMessage());
    }

    @Test
    public void Test_SubmitOrderWhenBuyOrderNotFilled() {
        // try {
        // // Arrange
        // Portfolio portfolio = new Portfolio(1L, "Google", "GOOGL", 20, 122.17,
        // 108.23, 0.0, 0.0);
        // Order invalidOrder = new Order(1L, "GOOGL", "SELL", 20, 100.0);
        // User dbUser = new User("leonlow", "Password0!", "leonlow@service.com",
        // 1000.0);
        // when(userRepository.findById(1L)).thenReturn(Optional.of(dbUser));

        // StockQuote stockQuote = new StockQuote(invalidOrder.getStockTicker());
        // Stock stock = mock(Stock.class);
        // when(stock.getQuote(true)).thenReturn(stockQuote);
        // when(stock.getName()).thenReturn("Google");
        // when(stock.getSymbol()).thenReturn("GOOGL");
        // // when(stock.getStock("GOOGL")).thenReturn(stock);
        // when(portfolioRepository.findByUserIdAndStockTicker(1L,
        // "GOOGL")).thenReturn(portfolio);

        // // Act
        // Throwable exception = assertThrows(ResourceNotFoundException.class, () -> {
        // orderServiceImpl.submitOrder(1, invalidOrder);
        // });

        // // Assert
        // assertEquals("Balance is insufficient to cover the total cost of stocks.",
        // exception.getMessage());
        // } catch (IOException e) {
        // fail("IOException was thrown: " + e.getMessage());
        // }
    }

}
