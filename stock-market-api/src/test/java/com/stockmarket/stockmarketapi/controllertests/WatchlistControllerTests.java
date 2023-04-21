package com.stockmarket.stockmarketapi.controllertests;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockmarket.stockmarketapi.controllers.WatchlistController;
import com.stockmarket.stockmarketapi.entity.Watchlist;
import com.stockmarket.stockmarketapi.service.WatchlistService;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@WebMvcTest(WatchlistController.class)
@AutoConfigureMockMvc(addFilters = false)
public class WatchlistControllerTests {

    private static final String GET_WATCHLIST_PATH = "/api/watchlist";
    private static final String ADD_WATCHLIST_PATH = "/api/watchlist/{stockTicker}";
    private static final String REMOVE_WATCHLIST_PATH = "/api/watchlist/{stockTicker}";

    private List<Watchlist> testWatchlist;
    private Watchlist testAddedWatchlist;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WatchlistService watchlistService;

    @BeforeEach
    void setup() {
        assertNotNull(mockMvc);
        assertNotNull(objectMapper);
        assertNotNull(watchlistService);

        testWatchlist = Arrays.asList(
                new Watchlist(1L, "Tesla, Inc.", "TSLA", 128.33, 125.13, 208.15, 200.1, 80.0, 0.0),
                new Watchlist(1L, "Alibaba Group Holding Limited", "BABA", 98.12, 96.54, 138.17,
                        58.7, 70.0, 0.0),
                new Watchlist(1L, "Apple Inc.", "AAPL", 134.50, 143.21, 162.89, 120.0, 65.0, 0.0));

        testAddedWatchlist = new Watchlist(1L, "JPMorgan Chase & Co.", "JPM", 138.47, 128.56,
                203.12, 120.23, 82.0, 2.73);
    }

    @Test
    public void Test_GetWatchlistShouldReturn200OK() throws Exception {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(watchlistService.getWatchlist(1)).thenReturn(testWatchlist);
        when(request.getAttribute("userId")).thenReturn(1);

        // Act and Assert
        mockMvc.perform(
                get(GET_WATCHLIST_PATH).accept(MediaType.APPLICATION_JSON).with(requestBuilder -> {
                    requestBuilder.setAttribute("userId", 1);
                    return requestBuilder;
                })).andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].stockName").value("Tesla, Inc."))
                .andExpect(jsonPath("$.[0].stockTicker").value("TSLA"))
                .andExpect(jsonPath("$.[0].price").value(128.33))
                .andExpect(jsonPath("$.[0].previousDayClose").value(125.13))
                .andExpect(jsonPath("$.[0]._52WeekHigh").value(208.15))
                .andExpect(jsonPath("$.[0]._52WeekLow").value(200.1))
                .andExpect(jsonPath("$.[0].marketCapInBillions").value(80.0))
                .andExpect(jsonPath("$.[0].annualDividendYield").value(0.0))
                .andExpect(jsonPath("$.[1].stockName").value("Alibaba Group Holding Limited"))
                .andExpect(jsonPath("$.[1].stockTicker").value("BABA"))
                .andExpect(jsonPath("$.[1].price").value(98.12))
                .andExpect(jsonPath("$.[1].previousDayClose").value(96.54))
                .andExpect(jsonPath("$.[1]._52WeekHigh").value(138.17))
                .andExpect(jsonPath("$.[1]._52WeekLow").value(58.7))
                .andExpect(jsonPath("$.[1].marketCapInBillions").value(70.0))
                .andExpect(jsonPath("$.[1].annualDividendYield").value(0.0))
                .andExpect(jsonPath("$.[2].stockName").value("Apple Inc."))
                .andExpect(jsonPath("$.[2].stockTicker").value("AAPL"))
                .andExpect(jsonPath("$.[2].price").value(134.50))
                .andExpect(jsonPath("$.[2].previousDayClose").value(143.21))
                .andExpect(jsonPath("$.[2]._52WeekHigh").value(162.89))
                .andExpect(jsonPath("$.[2]._52WeekLow").value(120.0))
                .andExpect(jsonPath("$.[2].marketCapInBillions").value(65.0))
                .andExpect(jsonPath("$.[2].annualDividendYield").value(0.0)).andReturn();

        verify(watchlistService, times(1)).getWatchlist(1);
    }

    @Test
    public void Test_AddWatchlistShouldReturn201Created() throws Exception {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("userId")).thenReturn(1);
        when(watchlistService.addStockWatchlist(1, "JPM")).thenReturn(testAddedWatchlist);

        // Act
        mockMvc.perform(post(ADD_WATCHLIST_PATH, "JPM").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testAddedWatchlist))
                .with(requestBuilder -> {
                    requestBuilder.setAttribute("userId", 1);
                    return requestBuilder;
                })).andExpect(jsonPath("$.stockName").value("JPMorgan Chase & Co."))
                .andExpect(jsonPath("$.stockTicker").value("JPM"))
                .andExpect(jsonPath("$.price").value(138.47))
                .andExpect(jsonPath("$.previousDayClose").value(128.56))
                .andExpect(jsonPath("$._52WeekHigh").value(203.12))
                .andExpect(jsonPath("$._52WeekLow").value(120.23))
                .andExpect(jsonPath("$.marketCapInBillions").value(82.0))
                .andExpect(jsonPath("$.annualDividendYield").value(2.73))
                .andExpect(status().isCreated()).andReturn();

        verify(watchlistService, times(1)).addStockWatchlist(1, "JPM");
    }

    @Test
    public void Test_RemoveWatchlistShouldReturn204NoContent() throws Exception {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("userId")).thenReturn(1);

        // Act and Assert
        mockMvc.perform(delete(REMOVE_WATCHLIST_PATH, "JPM").contentType(MediaType.APPLICATION_JSON)
                .with(requestBuilder -> {
                    requestBuilder.setAttribute("userId", 1);
                    return requestBuilder;
                })).andExpect(status().isNoContent()).andReturn();
    }
}
