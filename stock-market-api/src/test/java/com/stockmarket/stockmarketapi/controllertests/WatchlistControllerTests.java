package com.stockmarket.stockmarketapi.controllertests;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockmarket.stockmarketapi.entity.Watchlist;
import com.stockmarket.stockmarketapi.service.WatchlistService;
import com.stockmarket.stockmarketapi.web.WatchlistController;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
                new Watchlist(Long.valueOf(1), "Tesla, Inc.", "TSLA", 128.33, 125.13, 208.15, 200.1, 80.0, 0.0),
                new Watchlist(Long.valueOf(1), "Alibaba Group Holding Limited", "BABA", 98.12, 96.54, 138.17, 58.7,
                        70.0, 0.0),
                new Watchlist(Long.valueOf(1), "Apple Inc.", "AAPL", 134.50, 143.21, 162.89, 120.0, 65.0, 0.0));

        testAddedWatchlist = new Watchlist(Long.valueOf(1), "JPMorgan Chase & Co.", "JPM", 138.47, 128.56, 203.12,
                120.23, 82.0, 2.73);
    }

    @Test
    public void Test_GetWatchlistShouldReturn200OK() throws Exception {
        // Arrange
        when(watchlistService.getWatchlist(1)).thenReturn(testWatchlist);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("userId")).thenReturn(1);

        // Act
        MvcResult mvcResult = mockMvc.perform(
            get(GET_WATCHLIST_PATH).accept(MediaType.APPLICATION_JSON).with(requestBuilder -> {
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
            assertEquals(testWatchlist.get(i).getStockName(), jsonObject.getString("stockName"));
            assertEquals(testWatchlist.get(i).getStockTicker(), jsonObject.getString("stockTicker"));
            assertEquals(testWatchlist.get(i).getPrice(), Double.parseDouble(jsonObject.getString("price")));
            assertEquals(testWatchlist.get(i).getPreviousDayClose(), Double.parseDouble(jsonObject.getString("previousDayClose")));
            assertEquals(testWatchlist.get(i).get_52WeekHigh(), Double.parseDouble(jsonObject.getString("_52WeekHigh")));
            assertEquals(testWatchlist.get(i).get_52WeekLow(), Double.parseDouble(jsonObject.getString("_52WeekLow")));
            assertEquals(testWatchlist.get(i).getMarketCapInBillions(), Double.parseDouble(jsonObject.getString("marketCapInBillions")));
            assertEquals(testWatchlist.get(i).getAnnualDividendYield(), Double.parseDouble(jsonObject.getString("annualDividendYield")));
        }
    }

    @Test
    public void Test_AddWatchlistShouldReturn201Created() throws Exception {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("userId")).thenReturn(1);
        String requestBody = objectMapper.writeValueAsString(testAddedWatchlist);
        when(watchlistService.addStockWatchlist(1, "JPM")).thenReturn(testAddedWatchlist);

        // Act
        MvcResult mvcResult = mockMvc.perform(post(ADD_WATCHLIST_PATH, "JPM").contentType(MediaType.APPLICATION_JSON)
                .content(requestBody).with(requestBuilder -> {
                    requestBuilder.setAttribute("userId", 1);
                    return requestBuilder;
                })).andExpect(status().isCreated()).andReturn();

        // Assert
        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);

        Pattern pattern = Pattern.compile("/api/watchlist/([^\\s]+)");
        Matcher matcher = pattern.matcher(mvcResult.getRequest().getRequestURI());
        matcher.find();
        String pathParamStockTicker = matcher.group(1);

        // Calling the stockService method with the extracted path parameter stockTicker
        Watchlist returnedWatchlist = watchlistService.addStockWatchlist(1, pathParamStockTicker);
        assertEquals(testAddedWatchlist, returnedWatchlist);

        String responseBody = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(responseBody);
        assertEquals(testAddedWatchlist.getStockName(), jsonObject.getString("stockName"));
        assertEquals(testAddedWatchlist.getStockTicker(), jsonObject.getString("stockTicker"));
        assertEquals(testAddedWatchlist.getPrice(), Double.parseDouble(jsonObject.getString("price")));
        assertEquals(testAddedWatchlist.getPreviousDayClose(),
                Double.parseDouble(jsonObject.getString("previousDayClose")));
        assertEquals(testAddedWatchlist.get_52WeekHigh(), Double.parseDouble(jsonObject.getString("_52WeekHigh")));
        assertEquals(testAddedWatchlist.get_52WeekLow(), Double.parseDouble(jsonObject.getString("_52WeekLow")));
        assertEquals(testAddedWatchlist.getMarketCapInBillions(),
                Double.parseDouble(jsonObject.getString("marketCapInBillions")));
        assertEquals(testAddedWatchlist.getAnnualDividendYield(),
                Double.parseDouble(jsonObject.getString("annualDividendYield")));
    }

    @Test
    public void Test_RemoveWatchlistShouldReturn204NoContent() throws Exception {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("userId")).thenReturn(1);

        // Act
        MvcResult mvcResult = mockMvc.perform(
                delete(REMOVE_WATCHLIST_PATH, "JPM").contentType(MediaType.APPLICATION_JSON).with(requestBuilder -> {
                    requestBuilder.setAttribute("userId", 1);
                    return requestBuilder;
                }))
                .andExpect(status().isNoContent()).andReturn();

        // Assert
        int status = mvcResult.getResponse().getStatus();
        assertEquals(204, status);
    }
}
