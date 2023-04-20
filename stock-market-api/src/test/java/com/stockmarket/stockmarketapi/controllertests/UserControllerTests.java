package com.stockmarket.stockmarketapi.controllertests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockmarket.stockmarketapi.Constants;
import com.stockmarket.stockmarketapi.DTOs.UserAmountDTO;
import com.stockmarket.stockmarketapi.DTOs.UserLoginDTO;
import com.stockmarket.stockmarketapi.DTOs.UserRegisterDTO;
import com.stockmarket.stockmarketapi.controllers.UserController;
import com.stockmarket.stockmarketapi.entity.User;
import com.stockmarket.stockmarketapi.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTests {
    private static final String REGISTER_PATH = "/register";
    private static final String LOGIN_PATH = "/login";
    private static final String DEPOSIT_PATH = "/api/deposit";
    private static final String WITHDRAW_PATH = "/api/withdraw";

    private User testUser;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setup() {
        assertNotNull(mockMvc);
        assertNotNull(objectMapper);
        assertNotNull(userService);

        testUser = new User("leonlow", "Password0!", "leonlow@email.com", 1000.0, 1);
        testUser.setUserId(1L);
        testUser.setIsActive(1);
    }

    @Test
    public void Test_RegisterShouldReturn201Created() throws Exception {
        // Arrange
        UserRegisterDTO userRegisterDTO =
                new UserRegisterDTO("leonlow", "leonlow@email.com", "Password0!");
        when(userService.registerUser(any(UserRegisterDTO.class))).thenReturn(userRegisterDTO);
        String requestBody = objectMapper.writeValueAsString(testUser);

        // Act
        MvcResult mvcResult = mockMvc.perform(
                post(REGISTER_PATH).contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated()).andReturn();

        // Assert
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        String expectedJson = "{\"username\":\"leonlow\",\"email\":\"leonlow@email.com\"}";
        assertEquals(201, status);
        assertEquals(expectedJson, content);
    }

    @Test
    public void Test_LoginShouldReturn200OK() throws Exception {
        // Arrange
        when(userService.validateUser(any(UserLoginDTO.class))).thenReturn(testUser);
        String requestBody = objectMapper.writeValueAsString(testUser);

        // Act
        MvcResult mvcResult = mockMvc.perform(post(LOGIN_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(200, status);
        // Assert that the response contains a JWT token
        assertTrue(content.contains("accessToken"));
    }

    @Test
    public void Test_DepositUserBalance() throws Exception {
        // Arrange
        UserAmountDTO userAmountDTO = new UserAmountDTO();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("userId")).thenReturn(1);
        when(userService.updateUserBalance(1, userAmountDTO)).thenReturn(userAmountDTO);
        String requestBody = objectMapper.writeValueAsString(userAmountDTO);

        // Act
        MvcResult mvcResult =
                mockMvc.perform(put(DEPOSIT_PATH).contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody).with(requestBuilder -> {
                            requestBuilder.setAttribute("userId", 1);
                            return requestBuilder;
                        })).andExpect(status().isOk()).andReturn();

        // Assert
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        String expectedJson = String.format("{\"balance\":%.1f}", 3000.0);
        assertEquals(HttpStatus.OK.value(), status);
        assertEquals(expectedJson, content);
    }



    @Test
    public void Test_WithdrawUserBalance() throws Exception {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("userId")).thenReturn(1);
        String requestBody = objectMapper.writeValueAsString(testUser);

        // Act
        MvcResult mvcResult =
                mockMvc.perform(put(WITHDRAW_PATH).contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody).with(requestBuilder -> {
                            requestBuilder.setAttribute("userId", 1);
                            return requestBuilder;
                        })).andExpect(status().isOk()).andReturn();

        // Assert
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        String expectedJson = "{\"success\":true}";
        assertEquals(200, status);
        assertEquals(expectedJson, content);
    }

    @Test
    public void Test_GenerateJWTToken() {
        // Arrange
        testUser.setUserId(123L);

        // Act
        Map<String, String> tokenMap = UserController.generateJWTToken(testUser);

        // Assert
        assertTrue(tokenMap.containsKey("accessToken"));
        String token = tokenMap.get("accessToken");

        Claims claims = Jwts.parser().setSigningKey(Constants.API_SECRET_KEY).parseClaimsJws(token)
                .getBody();
        assertEquals(123L, claims.get("userId", Long.class));
        assertEquals("leonlow@email.com", claims.get("email", String.class));
        // Verify the token signature
        assertTrue(Jwts.parser().isSigned(token));
    }

}
