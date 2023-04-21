package com.stockmarket.stockmarketapi.controllertests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
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

    private User user = new User("leonlow", "Password0!", "leonlow@email.com", 1000.0, 1);
    private UserAmountDTO userAmountDTO = new UserAmountDTO();

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
    }

    @Test
    public void Test_RegisterShouldReturn201Created() throws Exception {
        // Arrange
        UserRegisterDTO userRegisterDTO =
                new UserRegisterDTO("leonlow", "leonlow@email.com", "Password0!");
        when(userService.registerUser(any(UserRegisterDTO.class))).thenReturn(userRegisterDTO);

        // Act and Assert
        mockMvc.perform(
                post(REGISTER_PATH).content(objectMapper.writeValueAsString(userRegisterDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("leonlow"))
                .andExpect(jsonPath("$.email").value("leonlow@email.com"))
                .andExpect(status().isCreated()).andReturn();

        verify(userService, times(1)).registerUser(any(UserRegisterDTO.class));
    }

    @Test
    public void Test_LoginShouldReturn200OK() throws Exception {
        // Arrange
        UserLoginDTO userLoginDTO = new UserLoginDTO("leonlow@email.com", "Password0!");
        when(userService.validateUser(any(UserLoginDTO.class))).thenReturn(user);

        // Act and Assert
        mockMvc.perform(post(LOGIN_PATH).content(objectMapper.writeValueAsString(userLoginDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").exists()).andExpect(status().isOk())
                .andReturn();

        verify(userService, times(1)).validateUser(any(UserLoginDTO.class));
    }

    @Test
    public void Test_DepositUserBalance() throws Exception {
        // Arrange
        userAmountDTO.setAmount(1000.0);
        userAmountDTO.setBalance(3000.0);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("userId")).thenReturn(1);
        when(userService.updateUserBalance(eq(1), ArgumentMatchers.any(UserAmountDTO.class)))
                .thenReturn(userAmountDTO);

        // Act and Assert
        mockMvc.perform(put(DEPOSIT_PATH).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userAmountDTO)).with(requestBuilder -> {
                    requestBuilder.setAttribute("userId", 1);
                    return requestBuilder;
                })).andExpect(jsonPath("$.balance").value(3000.0)).andExpect(status().isOk())
                .andReturn();

        verify(userService, times(1)).updateUserBalance(eq(1),
                ArgumentMatchers.any(UserAmountDTO.class));
    }

    @Test
    public void Test_WithdrawUserBalance() throws Exception {
        // Arrange
        userAmountDTO.setAmount(500.0);
        userAmountDTO.setBalance(500.0);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("userId")).thenReturn(1);
        when(userService.updateUserBalance(eq(1), ArgumentMatchers.any(UserAmountDTO.class)))
                .thenReturn(userAmountDTO);

        // Act and Assert
        mockMvc.perform(put(WITHDRAW_PATH).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userAmountDTO)).with(requestBuilder -> {
                    requestBuilder.setAttribute("userId", 1);
                    return requestBuilder;
                })).andExpect(jsonPath("$.balance").value(500.0)).andExpect(status().isOk())
                .andReturn();

        verify(userService, times(1)).updateUserBalance(eq(1),
                ArgumentMatchers.any(UserAmountDTO.class));
    }

    @Test
    public void Test_GenerateJWTToken() {
        // Arrange
        user.setUserId(123L);

        // Act
        Map<String, String> tokenMap = UserController.generateJWTToken(user);

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
