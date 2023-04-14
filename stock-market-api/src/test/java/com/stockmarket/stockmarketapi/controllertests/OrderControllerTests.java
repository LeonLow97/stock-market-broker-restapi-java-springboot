package com.stockmarket.stockmarketapi.controllertests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.stockmarket.stockmarketapi.Constants;
import com.stockmarket.stockmarketapi.entity.Order;
import com.stockmarket.stockmarketapi.entity.User;
import com.stockmarket.stockmarketapi.service.OrderService;
import com.stockmarket.stockmarketapi.service.UserService;
import com.stockmarket.stockmarketapi.web.OrderController;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.json.JSONObject;
import org.json.JSONArray;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import javax.servlet.http.HttpServletRequest;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
public class OrderControllerTests {

    private static final String GET_ALL_ORDERS_PATH = "/api/orders";
    private static final String GET_ORDER_PATH = "/api/orders/{orderId}";
    private static final String CREATE_ORDER_PATH = "/api/orders";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    public void Test_GetAllOrdersShouldReturn200OK() throws Exception {
        List<Order> testOrders =
                Arrays.asList(new Order(Long.valueOf(1), "AAPL", "BUY", 100, 166.7),
                        new Order(Long.valueOf(1), "GOOGL", "SELL", 50, 1250.5),
                        new Order(Long.valueOf(1), "TSLA", "BUY", 200, 735.0),
                        new Order(Long.valueOf(1), "AMZN", "SELL", 75, 3300.8));

        // Mock the service method to return the mock testOrders
        when(orderService.getAllOrders(1)).thenReturn(testOrders);

        // Set the userId attribute in the mock request
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("userId")).thenReturn(1);

        MvcResult mvcResult = mockMvc.perform(
                get(GET_ALL_ORDERS_PATH).accept(MediaType.APPLICATION_JSON).with(requestBuilder -> {
                    requestBuilder.setAttribute("userId", 1);
                    return requestBuilder;
                })).andExpect(status().isOk()).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        // response contains order date and the date value changes depending on when unit test is ran
        String responseBody = mvcResult.getResponse().getContentAsString();
        JSONArray jsonArray = new JSONArray(responseBody);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            assertNotNull(jsonObject.getString("orderDate"));
            assertEquals(testOrders.get(i).getStockTicker(), jsonObject.getString("stockTicker"));
            assertEquals(testOrders.get(i).getOrderType(), jsonObject.getString("orderType"));
            assertEquals(testOrders.get(i).getNoOfShares(), Integer.parseInt(jsonObject.getString("noOfShares")));
            assertEquals(testOrders.get(i).getCost(), Double.parseDouble(jsonObject.getString("cost")));
        }
    }

    @Test
    public void Test_GetOrderShouldReturn200OK() throws Exception {
        Order order = new Order(Long.valueOf(1), "BABA", "SELL", 210, 140.0);
    
        when(orderService.getOrder(1, 1)).thenReturn(order);
    
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("userId")).thenReturn(1);
    
        MvcResult mvcResult = mockMvc.perform(
                get(GET_ORDER_PATH, 1).accept(MediaType.APPLICATION_JSON).with(requestBuilder -> {
                    requestBuilder.setAttribute("userId", 1);
                    return requestBuilder;
                })).andExpect(status().isOk()).andReturn();
    
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        
        String responseBody = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(responseBody);
        assertNotNull(jsonObject.getString("orderDate"));
        assertEquals(order.getStockTicker(), jsonObject.getString("stockTicker"));
        assertEquals(order.getOrderType(), jsonObject.getString("orderType"));
        assertEquals(order.getNoOfShares(), Integer.parseInt(jsonObject.getString("noOfShares")));
        assertEquals(order.getCost(), Double.parseDouble(jsonObject.getString("cost")));
    }

}
