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
import com.stockmarket.stockmarketapi.entity.User;
import com.stockmarket.stockmarketapi.service.UserService;
import com.stockmarket.stockmarketapi.web.UserController;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTests {
    private static final String REGISTER_PATH = "/register";
    private static final String LOGIN_PATH = "/login";
    private static final String DEPOSIT_PATH = "/api/deposit";
    private static final String WITHDRAW_PATH = "/api/withdraw";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void Test_RegisterShouldReturn201Created() throws Exception {
        User user = new User();
        user.setUsername("leonlow");
        user.setPassword("Password0!");
        user.setEmail("leonlow@email.com");
        user.setBalance(1000.0);

        // Mock the registerUser method to return the registered user with userId
        Mockito.doAnswer(invocation -> {
            User registeredUser = invocation.getArgument(0);
            registeredUser.setUserId(1L);
            return registeredUser;
        }).when(userService).registerUser(Mockito.any(User.class));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // to enable Java 8 date/time type.
        String requestBody = objectMapper.writeValueAsString(user);

        // Perform the POST request to the "/register" endpoint
        MvcResult mvcResult = mockMvc.perform(
                post(REGISTER_PATH).contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated()).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
        String content = mvcResult.getResponse().getContentAsString();
        String expectedJson =
                "{\"userId\":1,\"username\":\"leonlow\",\"email\":\"leonlow@email.com\",\"balance\":1000.0,\"isActive\":0}";
        assertEquals(expectedJson, content);
    }

    @Test
    public void Test_LoginShouldReturn200OK() throws Exception {
        User user = new User();
        user.setUsername("");
        user.setBalance(0.0);
        user.setEmail("leonlow@email.com");
        user.setPassword("Password0!");

        // Mock the validateUser method to return the registered user with userId
        Mockito.when(userService.validateUser(Mockito.any(User.class))).thenAnswer(invocation -> {
            User registeredUser = invocation.getArgument(0);
            registeredUser.setUserId(1L);
            return registeredUser;
        });

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // to enable Java 8 date/time type.
        String requestBody = objectMapper.writeValueAsString(user);

        // Perform the POST request to the "/login" endpoint
        MvcResult mvcResult = mockMvc.perform(
                post(LOGIN_PATH).contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk()).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        // Assert that the response contains a JWT token
        assertTrue(content.contains("stock-market-token"));
    }

    @Test
    public void Test_DepositUserBalance() throws Exception {
        User user = new User();
        user.setUsername("leonlow");
        user.setPassword("Password0!");
        user.setEmail("leonlow@email.com");
        user.setBalance(3000.0);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("userId")).thenReturn(1);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // to enable Java 8 date/time type.
        String requestBody = objectMapper.writeValueAsString(user);

        // Perform the POST request to the "/register" endpoint
        MvcResult mvcResult = mockMvc.perform(
                put(DEPOSIT_PATH).contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk()).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    public void Test_WithdrawUserBalance() throws Exception {
        User user = new User();
        user.setUsername("leonlow");
        user.setPassword("Password0!");
        user.setEmail("leonlow@email.com");
        user.setBalance(3000.0);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("userId")).thenReturn(1);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // to enable Java 8 date/time type.
        String requestBody = objectMapper.writeValueAsString(user);
        
        // String requestBody = new ObjectMapper().writeValueAsString(user);

        // Perform the POST request to the "/register" endpoint
        MvcResult mvcResult = mockMvc.perform(
                put(WITHDRAW_PATH).contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk()).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    public void Test_GenerateJWTToken() {
        // Mock user object
        User user = new User();
        user.setUserId(123L);
        user.setEmail("leonlow@testing.com");

        // Call the method under test
        // JWTTokenGenerator generator = new JWTTokenGenerator();
        Map<String, String> tokenMap = UserController.generateJWTToken(user);

        // Verify that a token has been generated
        assertTrue(tokenMap.containsKey("stock-market-token"));
        String token = tokenMap.get("stock-market-token");

        // Verify the token claims
        Claims claims = Jwts.parser().setSigningKey(Constants.API_SECRET_KEY).parseClaimsJws(token)
                .getBody();
        assertEquals(123L, claims.get("userId", Long.class));
        assertEquals("leonlow@testing.com", claims.get("email", String.class));

        // Verify the token signature
        assertTrue(Jwts.parser().isSigned(token));
    }

}
