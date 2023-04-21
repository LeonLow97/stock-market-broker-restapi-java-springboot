package com.stockmarket.stockmarketapi.controllertests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.stockmarket.stockmarketapi.controllers.PortfolioController;
import com.stockmarket.stockmarketapi.entity.Portfolio;
import com.stockmarket.stockmarketapi.service.PortfolioService;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import javax.servlet.http.HttpServletRequest;

@WebMvcTest(PortfolioController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PortfolioControllerTests {

    private static final String GET_PORTFOLIO_PATH = "/api/portfolio";
    private static final String GET_PORTFOLIO_STOCK = "/api/portfolio/{portfolioId}";

    private List<Portfolio> portfolioList;
    private Portfolio portfolio;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PortfolioService portfolioService;

    @BeforeEach
    void setup() {
        assertNotNull(mockMvc);
        assertNotNull(portfolioService);

        portfolioList = Arrays.asList(
                new Portfolio(Long.valueOf(1), "Tesla, Inc.", "TSLA", 600, 128.33, 208.15, 80.0,
                        0.0),
                new Portfolio(Long.valueOf(1), "Alibaba Group Holding Limited", "BABA", 210, 98.12,
                        138.17, 70.0, 0.0),
                new Portfolio(Long.valueOf(1), "Apple Inc.", "AAPL", 45, 134.50, 162.89, 65.0,
                        0.0));

        portfolio =
                new Portfolio(Long.valueOf(1), "Apple Inc.", "AAPL", 45, 134.50, 162.89, 65.0, 0.0);
    }

    @Test
    public void Test_GetPortfolioShouldReturn200OK() throws Exception {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(portfolioService.getPortfolio(1)).thenReturn(portfolioList);
        when(request.getAttribute("userId")).thenReturn(1);

        // Act and Assert
        mockMvc.perform(
                get(GET_PORTFOLIO_PATH).accept(MediaType.APPLICATION_JSON).with(requestBuilder -> {
                    requestBuilder.setAttribute("userId", 1);
                    return requestBuilder;
                })).andExpect(jsonPath("$[0].stockName").value("Tesla, Inc."))
                .andExpect(jsonPath("$[0].stockTicker").value("TSLA"))
                .andExpect(jsonPath("$[0].noOfShares").value(600))
                .andExpect(jsonPath("$[0].cost").value(128.33))
                .andExpect(jsonPath("$[0].price").value(208.15))
                .andExpect(jsonPath("$[0].pnlinPercentage").value(80.0))
                .andExpect(jsonPath("$[0].pnlinDollars").value(0.0)).andExpect(status().isOk())
                .andExpect(jsonPath("$[1].stockName").value("Alibaba Group Holding Limited"))
                .andExpect(jsonPath("$[1].stockTicker").value("BABA"))
                .andExpect(jsonPath("$[1].noOfShares").value(210))
                .andExpect(jsonPath("$[1].cost").value(98.12))
                .andExpect(jsonPath("$[1].price").value(138.17))
                .andExpect(jsonPath("$[1].pnlinPercentage").value(70.0))
                .andExpect(jsonPath("$[1].pnlinDollars").value(0.0))
                .andExpect(jsonPath("$[2].stockName").value("Apple Inc."))
                .andExpect(jsonPath("$[2].stockTicker").value("AAPL"))
                .andExpect(jsonPath("$[2].noOfShares").value(45))
                .andExpect(jsonPath("$[2].cost").value(134.50))
                .andExpect(jsonPath("$[2].price").value(162.89))
                .andExpect(jsonPath("$[2].pnlinPercentage").value(65.0))
                .andExpect(jsonPath("$[2].pnlinDollars").value(0.0)).andReturn();

        verify(portfolioService, times(1)).getPortfolio(1);
    }

    @Test
    public void Test_GetPortfolioStockShouldReturn200OK() throws Exception {
        // Arrange
        when(portfolioService.getPortfolioStock(1, 1)).thenReturn(portfolio);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("userId")).thenReturn(1);

        // Act and Assert
        mockMvc.perform(
                get(GET_PORTFOLIO_STOCK, 1).accept(MediaType.APPLICATION_JSON).with(requestBuilder -> {
                    requestBuilder.setAttribute("userId", 1);
                return requestBuilder;
                })).andExpect(jsonPath("$.stockName").value("Apple Inc."))
                .andExpect(jsonPath("$.stockTicker").value("AAPL"))
                .andExpect(jsonPath("$.noOfShares").value(45))
                .andExpect(jsonPath("$.cost").value(134.50))
                .andExpect(jsonPath("$.price").value(162.89))
                .andExpect(jsonPath("$.pnlinPercentage").value(65.0))
                .andExpect(jsonPath("$.pnlinDollars").value(0.0))
                .andExpect(status().isOk()).andReturn();

        verify(portfolioService, times(1)).getPortfolioStock(1, 1);
    }

}
