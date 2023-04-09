package com.stockmarket.stockmarketapi.web;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.stockmarket.stockmarketapi.service.StockService;
import com.stockmarket.stockmarketapi.stockmodel.StockData;

@RestController
@RequestMapping("/yahoo-api")
public class StockController {

  @Autowired
  StockService stockService;

  @GetMapping("/stocks/{stockTicker}")
  public ResponseEntity<StockData> getStock(@PathVariable("stockTicker") String stockTicker) {
    try {
      StockData stockData = stockService.findStock(stockTicker);
      return new ResponseEntity<>(stockData, HttpStatus.OK);
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  // @GetMapping("/stocks")
  // public ResponseEntity<List<StockWrapper>> getPopularStocks() {
  // List<StockWrapper> stocks = stockService.findStocks(Arrays.asList("BABA", "GOOGL", "TSLA"));
  // System.out.println(stocks);
  // return new ResponseEntity<>(stocks, HttpStatus.OK);
  // }

}
