package com.stockmarket.stockmarketapi.repositorytests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.stockmarket.stockmarketapi.entity.Order;
import com.stockmarket.stockmarketapi.repository.OrderRepository;
import com.stockmarket.stockmarketapi.repository.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryTests {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void InjectedComponentsAreNotNull() {
        assertNotNull(orderRepository);
        assertNotNull(userRepository);
    }

    @Test
    public void Test_FindAllByUserIdSuccess() {
        // Arrange
        orderRepository.deleteAll();
        Order order1 = new Order(1L, "GOOGL", "BUY", 50, 108.31);
        Order order2 = new Order(1L, "TSLA", "SELL", 230, 1212.31);
        Order order3 = new Order(1L, "AAPL", "BUY", 30, 138.31);
        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);

        // Act
        List<Order> orders = orderRepository.findAllByUserId(1L);

        // Assert
        assertEquals(3, orders.size());
        assertTrue(orders.contains(order1));
        assertTrue(orders.contains(order2));
        assertTrue(orders.contains(order3));
    }

    @Test
    public void Test_FindAllByUserIdWhenUserDoesNotExist() {
        // Arrange
        Order order1 = new Order(1L, "GOOGL", "BUY", 50, 108.31);
        Order order2 = new Order(1L, "TSLA", "SELL", 230, 1212.31);
        Order order3 = new Order(1L, "AAPL", "BUY", 30, 138.31);
        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);

        // Act
        List<Order> orders = orderRepository.findAllByUserId(2L);

        // Assert
        assertTrue(orders.isEmpty());
    }

    @Test
    public void Test_FindByUserIdAndOrderIdSuccess() {
        // Arrange
        Order testOrder = new Order(1L, "GOOGL", "BUY", 50, 128.31);
        testOrder.setOrderId(7L);
        orderRepository.save(testOrder);

        // Act
        Order dbOrder = orderRepository.findByUserIdAndOrderId(1L, 7L);

        // Assert
        assertNotNull(dbOrder);
        assertEquals(dbOrder.getStockTicker(), testOrder.getStockTicker());
    }

    @Test
    public void Test_FindByUserIdAndOrderIdWhenBothIdsAreInvalid() {
        // Arrange
        Order testOrder = new Order(2L, "GOOGL", "BUY", 50, 128.31);
        testOrder.setOrderId(8L);
        orderRepository.save(testOrder);

        // Act
        Order dbOrder = orderRepository.findByUserIdAndOrderId(2L, 7L);

        // Assert
        assertNull(dbOrder);
        // assertEquals(dbOrder.getStockTicker(), testOrder.getStockTicker());
    }

}
