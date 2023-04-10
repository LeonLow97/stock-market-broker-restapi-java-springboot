package com.stockmarket.stockmarketapi.web;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.stockmarket.stockmarketapi.entity.Watchlist;
import com.stockmarket.stockmarketapi.service.WatchlistService;

@RestController
@RequestMapping("/api")
public class WatchlistController {

  @Autowired
  WatchlistService watchlistService;

  @PostMapping("/watchlist/{stockTicker}")
  public ResponseEntity<Watchlist> addWatchlist(HttpServletRequest request,
      @PathVariable("stockTicker") String stockTicker) {
    int userId = (Integer) request.getAttribute("userId");
    Watchlist watchlist = watchlistService.addWatchlist(userId, stockTicker);
    return new ResponseEntity<>(watchlist, HttpStatus.CREATED);
  }

}
