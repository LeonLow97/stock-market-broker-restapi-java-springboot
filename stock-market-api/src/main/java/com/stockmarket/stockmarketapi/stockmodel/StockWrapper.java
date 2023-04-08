package com.stockmarket.stockmarketapi.stockmodel;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.With;
import yahoofinance.Stock;

@Getter
@With
@AllArgsConstructor
public class StockWrapper {

  private final Stock stock;
  private final LocalDateTime lastAccessed;

  public StockWrapper(final Stock stock) {
    this.stock = stock;
    lastAccessed = LocalDateTime.now();
  }
  
}
