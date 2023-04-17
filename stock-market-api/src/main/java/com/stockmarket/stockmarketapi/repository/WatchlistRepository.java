package com.stockmarket.stockmarketapi.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.stockmarket.stockmarketapi.entity.Watchlist;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {

    List<Watchlist> findAllByUserId(long userId);

    Watchlist findByUserIdAndStockTicker(long userId, String stockTicker);

    void removeByUserIdAndStockTicker(long userId, String stockTicker);

}
