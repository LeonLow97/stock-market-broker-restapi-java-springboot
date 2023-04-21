package com.stockmarket.stockmarketapi.controllertests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.stockmarket.stockmarketapi.controllers.StockController;
import com.stockmarket.stockmarketapi.service.StockService;
import com.stockmarket.stockmarketapi.stockmodel.StockData;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import java.math.BigDecimal;

@WebMvcTest(StockController.class)
@AutoConfigureMockMvc(addFilters = false)
public class StockControllerTests {

    private static final String GET_STOCK_PATH = "/yahoo-api/stocks/{stockTicker}";

    private StockData stockData;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockService stockService;

    @BeforeEach
    void setup() {
        assertNotNull(mockMvc);
        assertNotNull(stockService);

        // Mocking the stock data with all arguments constructor
        BigDecimal previousDayClose = new BigDecimal("96.17");
        BigDecimal stockPrice = new BigDecimal("94.55");
        BigDecimal dayHigh = new BigDecimal("96.12");
        BigDecimal dayLow = new BigDecimal("93.84");
        BigDecimal dayChangeInPercent = new BigDecimal("-1.68");
        BigDecimal _52WeekChangeInPercent = new BigDecimal("-24.86");
        BigDecimal _52WeekHigh = new BigDecimal("125.84");
        BigDecimal yearDividendInPercent = new BigDecimal("0.0");
        stockData = new StockData("Alibaba Group Holding Limited", "BABA", previousDayClose,
                stockPrice, dayHigh, dayLow, dayChangeInPercent, _52WeekChangeInPercent,
                _52WeekHigh, yearDividendInPercent);
    }

    @Test
    public void Test_GetStockReturn200OK() throws Exception {
        // Arrange
        when(stockService.findStock("BABA")).thenReturn(stockData);

        // Act and Assert
        mockMvc.perform(get(GET_STOCK_PATH, "BABA")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.stockName").value("Alibaba Group Holding Limited"))
            .andExpect(jsonPath("$.stockTicker").value("BABA"))
            .andExpect(jsonPath("$.previousDayClose").value("96.17"))
            .andExpect(jsonPath("$.stockPrice").value("94.55"))
            .andExpect(jsonPath("$.dayHigh").value("96.12"))
            .andExpect(jsonPath("$.dayLow").value("93.84"))
            .andExpect(jsonPath("$.dayChangeInPercent").value("-1.68"))
            .andExpect(jsonPath("$._52WeekChangeInPercent").value("-24.86"))
            .andExpect(jsonPath("$._52WeekHigh").value("125.84"))
            .andExpect(jsonPath("$.yearDividendInPercent").value("0.0"))
            .andExpect(status().isOk())
            .andReturn();

        verify(stockService, times(1)).findStock("BABA");
    }

}
