package com.stockmarket.stockmarketapi.service;

import java.io.IOException;
import org.springframework.stereotype.Service;
import com.stockmarket.stockmarketapi.exception.BadRequestException;
import com.stockmarket.stockmarketapi.stockmodel.StockData;
import com.stockmarket.stockmarketapi.stockmodel.StockWrapper;
import lombok.AllArgsConstructor;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

@AllArgsConstructor
@Service
public class StockService {

  public StockData findStock(final String ticker) throws IOException {
    try {
      Stock stock = YahooFinance.get(ticker.trim());
      StockWrapper stockWrapper = new StockWrapper(stock);

      if (stock == null) {
        throw new BadRequestException("Stock ticker does not exist.");
      }

      StockData stockData = new StockData();
      stockData.setStockName(stock.getName());
      stockData.setStockTicker(stockWrapper.getStock().getQuote().getSymbol());
      stockData.setPreviousDayClose(stockWrapper.getStock().getQuote().getPreviousClose());
      stockData.setStockPrice(stockWrapper.getStock().getQuote(true).getPrice());
      stockData.setDayHigh(stockWrapper.getStock().getQuote().getDayHigh());
      stockData.setDayLow(stockWrapper.getStock().getQuote().getDayLow());
      stockData.setDayChangeInPercent(stockWrapper.getStock().getQuote().getChangeInPercent());
      stockData.set_52WeekChangeInPercent(stockWrapper.getStock().getQuote().getChangeFromYearHighInPercent());
      stockData.set_52WeekHigh(stockWrapper.getStock().getQuote().getYearHigh());
      stockData.setYearDividendInPercent(stockWrapper.getStock().getDividend().getAnnualYieldPercent());

      return stockData;
      // return new StockWrapper(YahooFinance.get(ticker));
    } catch (IOException e) {
      throw new IOException("Failed to connect to Yahoo Finance API");
    }
  }

}
