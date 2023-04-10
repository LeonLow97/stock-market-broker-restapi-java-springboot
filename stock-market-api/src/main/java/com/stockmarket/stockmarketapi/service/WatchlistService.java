package com.stockmarket.stockmarketapi.service;

import java.util.List;
import com.stockmarket.stockmarketapi.entity.Watchlist;

public interface WatchlistService {

  List<Watchlist> getWatchlist(int userId);

  Watchlist addStockWatchlist(int userId, String stockTicker);

  void removeStockWatchlist(int userId, String stockTicker);

}
