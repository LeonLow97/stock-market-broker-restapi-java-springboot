package com.stockmarket.stockmarketapi.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.stockmarket.stockmarketapi.stockmodel.StockWrapper;
import lombok.AllArgsConstructor;
import yahoofinance.YahooFinance;

@AllArgsConstructor
@Service
public class StockService {

  public StockWrapper findStock(final String ticker) {
    try {
      return new StockWrapper(YahooFinance.get(ticker));
    } catch (IOException e) {
      System.out.println("Error in finding stock: " + e.getMessage());
    }
    return null;
  }

  public BigDecimal findPrice(final StockWrapper stock) throws IOException {
    try {
      return stock.getStock().getQuote(true).getPrice();
    } catch (IOException e) {
      System.out.println(e.getMessage());
      throw new IOException("IOException.");
    }
  }

  public List<StockWrapper> findStocks(final List<String> tickers) {
    return tickers.stream().map(this::findStock).filter(Objects::nonNull).collect(Collectors.toList());
  }

}
