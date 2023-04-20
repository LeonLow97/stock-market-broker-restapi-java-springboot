package com.stockmarket.stockmarketapi.service;

import java.io.IOException;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stockmarket.stockmarketapi.entity.Watchlist;
import com.stockmarket.stockmarketapi.exception.InternalServerErrorException;
import com.stockmarket.stockmarketapi.exception.ResourceAlreadyExistsException;
import com.stockmarket.stockmarketapi.exception.ResourceNotFoundException;
import com.stockmarket.stockmarketapi.repository.WatchlistRepository;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

@Service
@Transactional
public class WatchlistServiceImpl implements WatchlistService {

  @Autowired
  WatchlistRepository watchlistRepository;

  @Override
  public List<Watchlist> getWatchlist(int userId) {
    List<Watchlist> watchlist = watchlistRepository.findAllByUserId(Long.valueOf(userId));
    if (watchlist == null) {
      throw new ResourceNotFoundException("Watchlist is empty");
    }
    return watchlist;
  }

  public Watchlist addStockWatchlist(int userId, String stockTicker) {
    try {
      Stock stock = YahooFinance.get(stockTicker.trim());
      if (stock == null) {
        throw new ResourceNotFoundException("Stock Ticker does not exist.");
      }

      Watchlist dbWatchlist =
          watchlistRepository.findByUserIdAndStockTicker(Long.valueOf(userId), stockTicker);
      if (dbWatchlist != null) {
        throw new ResourceAlreadyExistsException("Stock already in watchlist.");
      }

      Watchlist watchlist = new Watchlist();
      watchlist.setUserId(Long.valueOf(userId));
      watchlist.setStockName(stock.getName());
      watchlist.setStockTicker(stock.getQuote().getSymbol());
      watchlist.setPrice(stock.getQuote().getPrice().doubleValue());
      watchlist.setPreviousDayClose(stock.getQuote().getPreviousClose().doubleValue());
      watchlist.set_52WeekHigh(stock.getQuote().getYearHigh().doubleValue());
      watchlist.set_52WeekLow(stock.getQuote().getYearLow().doubleValue());
      watchlist
          .setMarketCapInBillions(stock.getStats().getMarketCap().doubleValue() / 1_000_000_000.0);
      watchlist.setAnnualDividendYield(stock.getDividend().getAnnualYieldPercent().doubleValue());

      Watchlist addedWatchlist = watchlistRepository.save(watchlist);
      return addedWatchlist;
    } catch (IOException e) {
      System.out.println("IOException YahooFinanceAPI addWatchlist: " + e.getMessage());
      throw new InternalServerErrorException("Internal Server Error");
    }
  }

  @Override
  public void removeStockWatchlist(int userId, String stockTicker) {
    watchlistRepository.removeByUserIdAndStockTicker(Long.valueOf(userId), stockTicker);
  }

}
