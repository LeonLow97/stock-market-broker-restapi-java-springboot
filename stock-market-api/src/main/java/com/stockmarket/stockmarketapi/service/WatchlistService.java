package com.stockmarket.stockmarketapi.service;

import java.util.List;
import com.stockmarket.stockmarketapi.entity.Watchlist;
import com.stockmarket.stockmarketapi.exception.ResourceAlreadyExistsException;
import com.stockmarket.stockmarketapi.exception.ResourceNotFoundException;

public interface WatchlistService {

  List<Watchlist> getWatchlist(int userId) throws ResourceNotFoundException;
  Watchlist addStockWatchlist(int userId, String stockTicker) throws ResourceAlreadyExistsException, ResourceNotFoundException;
  void removeStockWatchlist(int userId, String stockTicker);
  
}
