package com.stockmarket.stockmarketapi.controllertests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.stockmarket.stockmarketapi.service.StockService;
import com.stockmarket.stockmarketapi.stockmodel.StockData;
import com.stockmarket.stockmarketapi.web.StockController;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebMvcTest(StockController.class)
@AutoConfigureMockMvc(addFilters = false)
public class StockControllerTests {

    private static final String GET_STOCK_PATH = "/yahoo-api/stocks/{stockTicker}";

    private StockData testStockData;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockService stockService;

    @BeforeEach
    void setup() {
        // Mocking the stock data with all arguments constructor
        BigDecimal previousDayClose = new BigDecimal("96.17");
        BigDecimal stockPrice = new BigDecimal("94.55");
        BigDecimal dayHigh = new BigDecimal("96.12");
        BigDecimal dayLow = new BigDecimal("93.84");
        BigDecimal dayChangeInPercent = new BigDecimal("-1.68");
        BigDecimal _52WeekChangeInPercent = new BigDecimal("-24.86");
        BigDecimal _52WeekHigh = new BigDecimal("125.84");
        BigDecimal yearDividendInPercent = new BigDecimal("0.0");
        testStockData = new StockData("Alibaba Group Holding Limited", "BABA",
                previousDayClose, stockPrice, dayHigh, dayLow, dayChangeInPercent,
                _52WeekChangeInPercent, _52WeekHigh, yearDividendInPercent);
    }

    @Test
    public void Test_GetStockReturn200OK() throws Exception {
        // Arrange
        when(stockService.findStock("BABA")).thenReturn(testStockData);

        // Act
        MvcResult mvcResult = mockMvc.perform(get(GET_STOCK_PATH, "BABA")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        // Assert
        // Using regex to extract path parameter (pattern for getting one or more non-whitespace characters)
        Pattern pattern = Pattern.compile("/yahoo-api/stocks/([^\\s]+)");
        Matcher matcher = pattern.matcher(mvcResult.getRequest().getRequestURI());
        matcher.find();
        String pathParamStockTicker = matcher.group(1);

        // Calling the stockService method with the extracted path parameter stockTicker
        StockData returnedStockData = stockService.findStock(pathParamStockTicker);
        assertEquals(testStockData, returnedStockData);

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        assertEquals(testStockData.getStockName(), returnedStockData.getStockName());
        assertEquals(testStockData.getStockTicker(), returnedStockData.getStockTicker());
        assertEquals(testStockData.getPreviousDayClose().toString(), returnedStockData.getPreviousDayClose().toString());
        assertEquals(testStockData.getStockPrice().toString(), returnedStockData.getStockPrice().toString());
        assertEquals(testStockData.getDayHigh().toString(), returnedStockData.getDayHigh().toString());
        assertEquals(testStockData.getDayLow().toString(), returnedStockData.getDayLow().toString());
        assertEquals(testStockData.getDayChangeInPercent().toString(), returnedStockData.getDayChangeInPercent().toString());
        assertEquals(testStockData.get_52WeekChangeInPercent().toString(), returnedStockData.get_52WeekChangeInPercent().toString());
        assertEquals(testStockData.get_52WeekHigh().toString(), returnedStockData.get_52WeekHigh().toString());
        assertEquals(testStockData.getYearDividendInPercent().toString(), returnedStockData.getYearDividendInPercent().toString());
    }

}
