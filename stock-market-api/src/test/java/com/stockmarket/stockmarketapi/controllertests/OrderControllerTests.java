package com.stockmarket.stockmarketapi.controllertests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockmarket.stockmarketapi.entity.Order;
import com.stockmarket.stockmarketapi.service.OrderService;
import com.stockmarket.stockmarketapi.web.OrderController;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.json.JSONArray;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import javax.servlet.http.HttpServletRequest;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
public class OrderControllerTests {

    private static final String GET_ALL_ORDERS_PATH = "/api/orders";
    private static final String GET_ORDER_PATH = "/api/orders/{orderId}";
    private static final String SUBMIT_ORDER_PATH = "/api/orders";

    private List<Order> testOrders;
    private Order testOrder;
    private Order testSubmitOrder;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @BeforeEach
    void setup() {
        assertNotNull(mockMvc);
        assertNotNull(objectMapper);
        assertNotNull(orderService);
        
        testOrders = Arrays.asList(new Order(Long.valueOf(1), "AAPL", "BUY", 100, 166.7),
                new Order(Long.valueOf(1), "GOOGL", "SELL", 50, 1250.5),
                new Order(Long.valueOf(1), "TSLA", "BUY", 200, 735.0),
                new Order(Long.valueOf(1), "AMZN", "SELL", 75, 3300.8));

        testOrder = new Order(Long.valueOf(1), "BABA", "SELL", 210, 140.0);
        testOrder.setOrderId(1L);
        testSubmitOrder = new Order(Long.valueOf(1), "TSLA", "SELL", 65, 1088.5);
        testSubmitOrder.setOrderId(1L);
    }

    @Test
    public void Test_GetAllOrdersShouldReturn200OK() throws Exception {
        // Arrange
        when(orderService.getAllOrders(1)).thenReturn(testOrders);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("userId")).thenReturn(1);

        // Act
        MvcResult mvcResult = mockMvc.perform(
                get(GET_ALL_ORDERS_PATH).accept(MediaType.APPLICATION_JSON).with(requestBuilder -> {
                    requestBuilder.setAttribute("userId", 1);
                    return requestBuilder;
                })).andExpect(status().isOk()).andReturn();

        // Assert
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String responseBody = mvcResult.getResponse().getContentAsString();
        JSONArray jsonArray = new JSONArray(responseBody);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            assertNotNull(jsonObject.getString("orderDate"));
            assertEquals(testOrders.get(i).getStockTicker(), jsonObject.getString("stockTicker"));
            assertEquals(testOrders.get(i).getOrderType(), jsonObject.getString("orderType"));
            assertEquals(testOrders.get(i).getNoOfShares(),
                    Integer.parseInt(jsonObject.getString("noOfShares")));
            assertEquals(testOrders.get(i).getCost(),
                    Double.parseDouble(jsonObject.getString("cost")));
        }
    }

    @Test
    public void Test_GetOrderShouldReturn200OK() throws Exception {
        // Arrange
        when(orderService.getOrder(1, 1)).thenReturn(testOrder);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("userId")).thenReturn(1);

        // Act
        MvcResult mvcResult = mockMvc.perform(
                // Adding params to the URL
                get(GET_ORDER_PATH, 1).accept(MediaType.APPLICATION_JSON).with(requestBuilder -> {
                    requestBuilder.setAttribute("userId", 1);
                    return requestBuilder;
                })).andExpect(status().isOk()).andReturn();

        // Assert
        // Extract value from path parameter (regex to capture one or more digits 0-9)
        Pattern pattern = Pattern.compile("/api/orders/(\\d+)");
        Matcher matcher = pattern.matcher(mvcResult.getRequest().getRequestURI());
        matcher.find();
        int orderId = Integer.parseInt(matcher.group(1));

        // Calling the orderService method with the extracted path parameter orderId
        Order returnedOrder = orderService.getOrder(1, orderId);
        assertEquals(testOrder, returnedOrder);

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String responseBody = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(responseBody);
        assertNotNull(jsonObject.getString("orderDate"));
        assertEquals(testOrder.getStockTicker(), jsonObject.getString("stockTicker"));
        assertEquals(testOrder.getOrderType(), jsonObject.getString("orderType"));
        assertEquals(testOrder.getNoOfShares(), Integer.parseInt(jsonObject.getString("noOfShares")));
        assertEquals(testOrder.getCost(), Double.parseDouble(jsonObject.getString("cost")));
    }

    @Test
    public void Test_SubmitOrderReturn201Created() throws Exception {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("userId")).thenReturn(1);
        String requestBody = objectMapper.writeValueAsString(testSubmitOrder);

        // Act
        MvcResult mvcResult = mockMvc.perform(post(SUBMIT_ORDER_PATH).contentType(MediaType.APPLICATION_JSON)
                .content(requestBody).with(requestBuilder -> {
                    requestBuilder.setAttribute("userId", 1);
                    return requestBuilder;
                })).andExpect(status().isCreated()).andReturn();

        // Assert
        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
        String responseBody = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(responseBody);
        assertNotNull(jsonObject.getString("orderDate"));
        assertEquals(testSubmitOrder.getStockTicker(), jsonObject.getString("stockTicker"));
        assertEquals(testSubmitOrder.getOrderType(), jsonObject.getString("orderType"));
        assertEquals(testSubmitOrder.getNoOfShares(), Integer.parseInt(jsonObject.getString("noOfShares")));
        assertEquals(testSubmitOrder.getCost(), Double.parseDouble(jsonObject.getString("cost")));
    }

}
