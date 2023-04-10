package com.stockmarket.stockmarketapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.stockmarket.stockmarketapi.entity.Watchlist;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
  
    Watchlist findByUserIdAndStockTicker(Long userId, String stockTicker);

}
