package com.stockmarket.stockmarketapi;

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
import com.stockmarket.stockmarketapi.entity.User;
import com.stockmarket.stockmarketapi.service.UserService;
import com.stockmarket.stockmarketapi.web.UserController;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTests {
    private static final String REGISTER_PATH = "/register";
    private static final String LOGIN_PATH = "/login";

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

        String requestBody = new ObjectMapper().writeValueAsString(user);

        // Perform the POST request to the "/register" endpoint
        MvcResult mvcResult = mockMvc
                .perform(post(REGISTER_PATH).contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated()).andDo(print()).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
        String content = mvcResult.getResponse().getContentAsString();
        String expectedJson =
                "{\"userId\":1,\"username\":\"leonlow\",\"email\":\"leonlow@email.com\",\"balance\":1000.0,\"isActive\":0}";
        assertEquals(content, expectedJson);
    }

    @Test
    public void Test_LoginShouldReturn200OK() throws Exception {
        User user = new User();
        user.setUsername("leonlow");
        user.setBalance(1000.0);

        user.setEmail("leonlow@email.com");
        user.setPassword("Password0!");

        // Mock the validateUser method to return the registered user with userId
        Mockito.doAnswer(invocation -> {
            User registeredUser = invocation.getArgument(0);
            registeredUser.setUserId(1L);
            return registeredUser;
        }).when(userService).validateUser(Mockito.any(User.class));

        String requestBody = new ObjectMapper().writeValueAsString(user);

        // Perform the POST request to the "/login" endpoint
        MvcResult mvcResult = mockMvc
                .perform(post(LOGIN_PATH).contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk()).andDo(print()).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        // Assert that the response contains a JWT token
        assertTrue(content.contains("stock-market-token"));
    }

}
