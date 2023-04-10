package com.stockmarket.stockmarketapi.service;

import com.stockmarket.stockmarketapi.entity.Watchlist;
import com.stockmarket.stockmarketapi.exception.BadRequestException;
import com.stockmarket.stockmarketapi.exception.ResourceNotFoundException;

public interface WatchlistService {

  Watchlist addWatchlist(int userId, String stockTicker) throws BadRequestException, ResourceNotFoundException;
  
}
