package com.stockmarket.stockmarketapi.controllers;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

  @GetMapping("/watchlist")
  public ResponseEntity<List<Watchlist>> getWatchlist(HttpServletRequest request) {
    int userId = (Integer) request.getAttribute("userId");
    List<Watchlist> watchlist = watchlistService.getWatchlist(userId);
    return new ResponseEntity<>(watchlist, HttpStatus.OK);
  }

  @PostMapping("/watchlist/{stockTicker}")
  public ResponseEntity<Watchlist> addWatchlist(HttpServletRequest request,
      @PathVariable("stockTicker") String stockTicker) {
    int userId = (Integer) request.getAttribute("userId");
    Watchlist watchlist = watchlistService.addStockWatchlist(userId, stockTicker);
    return new ResponseEntity<>(watchlist, HttpStatus.CREATED);
  }

  @DeleteMapping("/watchlist/{stockTicker}")
  public ResponseEntity<HttpStatus> removeWatchlist(HttpServletRequest request, @PathVariable("stockTicker") String stockTicker) {
    int userId = (Integer) request.getAttribute("userId");
    watchlistService.removeStockWatchlist(userId, stockTicker);
    return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
  }
}
