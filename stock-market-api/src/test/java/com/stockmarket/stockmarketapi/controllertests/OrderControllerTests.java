package com.stockmarket.stockmarketapi.controllertests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockmarket.stockmarketapi.DTOs.OrderSubmitDTO;
import com.stockmarket.stockmarketapi.controllers.OrderController;
import com.stockmarket.stockmarketapi.entity.Order;
import com.stockmarket.stockmarketapi.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import javax.servlet.http.HttpServletRequest;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
public class OrderControllerTests {

    private static final String GET_ALL_ORDERS_PATH = "/api/orders";
    private static final String GET_ORDER_PATH = "/api/orders/{orderId}";
    private static final String SUBMIT_ORDER_PATH = "/api/orders";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    private List<Order> orders = Arrays.asList(new Order(1L, "AAPL", "BUY", 100, 166.7),
            new Order(1L, "GOOGL", "SELL", 50, 1250.5), new Order(1L, "TSLA", "BUY", 200, 735.0),
            new Order(1L, "AMZN", "SELL", 75, 3300.8));

    private Order order = new Order(1L, "JPM", "BUY", 600, 123.43);

    @BeforeEach
    void setup() {
        assertNotNull(mockMvc);
        assertNotNull(objectMapper);
        assertNotNull(orderService);
    }

    @Test
    public void Test_GetAllOrdersShouldReturn200OK() throws Exception {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(orderService.getAllOrders(1)).thenReturn(orders);
        when(request.getAttribute("userId")).thenReturn(1);

        // Act and Assert
        mockMvc.perform(
                get(GET_ALL_ORDERS_PATH).accept(MediaType.APPLICATION_JSON).with(requestBuilder -> {
                    requestBuilder.setAttribute("userId", 1);
                    return requestBuilder;
                })).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(orders.size()))
                .andExpect(jsonPath("$[0].stockTicker").value("AAPL"))
                .andExpect(jsonPath("$[0].orderType").value("BUY"))
                .andExpect(jsonPath("$[0].noOfShares").value(100))
                .andExpect(jsonPath("$[0].cost").value(166.7))
                .andExpect(jsonPath("$[1].stockTicker").value("GOOGL"))
                .andExpect(jsonPath("$[1].orderType").value("SELL"))
                .andExpect(jsonPath("$[1].noOfShares").value(50))
                .andExpect(jsonPath("$[1].cost").value(1250.5))
                .andExpect(jsonPath("$[2].stockTicker").value("TSLA"))
                .andExpect(jsonPath("$[2].orderType").value("BUY"))
                .andExpect(jsonPath("$[2].noOfShares").value(200))
                .andExpect(jsonPath("$[2].cost").value(735.0))
                .andExpect(jsonPath("$[3].stockTicker").value("AMZN"))
                .andExpect(jsonPath("$[3].orderType").value("SELL"))
                .andExpect(jsonPath("$[3].noOfShares").value(75))
                .andExpect(jsonPath("$[3].cost").value(3300.8)).andReturn();

        verify(orderService, times(1)).getAllOrders(1);
    }

    @Test
    public void Test_GetOrderShouldReturn200OK() throws Exception {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(orderService.getOrder(1, 1)).thenReturn(order);
        when(request.getAttribute("userId")).thenReturn(1);

        // Act and Assert
        mockMvc.perform(
                // Adding params to the URL
                get(GET_ORDER_PATH, 1).accept(MediaType.APPLICATION_JSON).with(requestBuilder -> {
                    requestBuilder.setAttribute("userId", 1);
                    return requestBuilder;
                })).andExpect(status().isOk()).andExpect(jsonPath("$.stockTicker").value("JPM"))
                .andExpect(jsonPath("$.orderType").value("BUY"))
                .andExpect(jsonPath("$.noOfShares").value(600))
                .andExpect(jsonPath("$.cost").value(123.43)).andReturn();

        verify(orderService, times(1)).getOrder(1, 1);
    }

    @Test
    public void Test_SubmitOrderReturn201Created() throws Exception {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        OrderSubmitDTO orderSubmitDTO = new OrderSubmitDTO("GOOGL", "BUY", 260, 100.1);
        Order order = new Order(1, "GOOGL", "BUY", 260, 100.1);
        when(request.getAttribute("userId")).thenReturn(1);
        when(orderService.submitOrder(eq(1), ArgumentMatchers.any(OrderSubmitDTO.class)))
                .thenReturn(order); // use ArgumentMatchers

        // Act and Assert
        mockMvc.perform(
                post(SUBMIT_ORDER_PATH).content(objectMapper.writeValueAsString(orderSubmitDTO))
                        .contentType(MediaType.APPLICATION_JSON).with(requestBuilder -> {
                            requestBuilder.setAttribute("userId", 1);
                            return requestBuilder;
                        }))
                .andExpect(jsonPath("$.stockTicker").value("GOOGL"))
                .andExpect(jsonPath("$.orderType").value("BUY"))
                .andExpect(jsonPath("$.noOfShares").value(260))
                .andExpect(jsonPath("$.cost").value(100.1)).andExpect(status().isCreated())
                .andReturn();

        verify(orderService, times(1)).submitOrder(eq(1), ArgumentMatchers.any(OrderSubmitDTO.class));
    }
}
