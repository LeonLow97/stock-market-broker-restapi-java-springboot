package com.stockmarket.stockmarketapi.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.stockmarket.stockmarketapi.service.StockService;
import com.stockmarket.stockmarketapi.stockmodel.StockData;
import com.stockmarket.stockmarketapi.stockmodel.StockWrapper;
import yahoofinance.YahooFinance;

@RestController
@RequestMapping("/yahoo-api")
public class StockController {

  @Autowired
  StockService stockService;
  
  @GetMapping("/stocks/{stockTicker}")
  public ResponseEntity<StockData> getStock(@PathVariable("stockTicker") String stockTicker) {
    try {
      StockWrapper stock = stockService.findStock(stockTicker);
      BigDecimal price = stockService.findPrice(stock);
      StockData stockData = new StockData();
      stockData.setStockPrice(price);
      stockData.setStockTicker(stockTicker);
      stockData.setStockName(YahooFinance.get(stockTicker).getName());
      return new ResponseEntity<>(stockData, HttpStatus.OK);
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  @GetMapping("/stocks")
  public ResponseEntity<List<StockWrapper>> getPopularStocks() {
    // List<StockWrapper> stocks = stockService.findStocks(Arrays.asList("BABA", "GOOGL", "TSLA"));
    // System.out.println(stocks);
    // return new ResponseEntity<>(stocks, HttpStatus.OK);
  }

}
