package com.stockmarket.stockmarketapi.controllers;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.stockmarket.stockmarketapi.entity.Watchlist;
import com.stockmarket.stockmarketapi.service.WatchlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Watchlist Controller",
    description = "Allow user to save stocks into their watchlist to monitor prices or other details related to the stock.")
@RestController
@RequestMapping("/api")
public class WatchlistController {

  @Autowired
  WatchlistService watchlistService;

  @Operation(summary = "Get Watchlist",
      description = "Retrieves the watchlist of the user for all the stocks")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Successfully retrieves the watchlist for all the stocks.",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = Watchlist.class)))),
      @ApiResponse(responseCode = "404", description = "NOT FOUND - Watchlist is empty",
          content = @Content)})
  @GetMapping(value = "/watchlist", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Watchlist>> getWatchlist(HttpServletRequest request) {
    int userId = (Integer) request.getAttribute("userId");
    List<Watchlist> watchlist = watchlistService.getWatchlist(userId);
    return new ResponseEntity<>(watchlist, HttpStatus.OK);
  }

  @Operation(summary = "Add Watchlist", description = "Adds a stock the the watchlist of the user.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201",
          description = "Successfully added the stock to the watchlist of the user.",
          content = @Content(schema = @Schema(implementation = Watchlist.class))),
      @ApiResponse(responseCode = "404", description = "NOT FOUND - Stock Ticker is invalid",
          content = @Content),
      @ApiResponse(responseCode = "409", description = "CONFLICT - Stock is already in watchlist",
          content = @Content),
      @ApiResponse(responseCode = "500",
          description = "INTERNAL SERVER ERROR - Unable to connect to Yahoo Finance API",
          content = @Content)})
  @PostMapping(value = "/watchlist/{stockTicker}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Watchlist> addWatchlist(HttpServletRequest request,
      @PathVariable("stockTicker") String stockTicker) {
    int userId = (Integer) request.getAttribute("userId");
    Watchlist watchlist = watchlistService.addStockWatchlist(userId, stockTicker);
    return new ResponseEntity<>(watchlist, HttpStatus.CREATED);
  }

  @Operation(summary = "Remove Watchlist",
      description = "Removes a stock from the watchlist of the user.")
  @ApiResponses(value = {@ApiResponse(responseCode = "204",
      description = "Successfully deleted the stock from the watchlist of the user.",
      content = @Content)})
  @DeleteMapping("/watchlist/{stockTicker}")
  public ResponseEntity<HttpStatus> removeWatchlist(HttpServletRequest request,
      @PathVariable("stockTicker") String stockTicker) {
    int userId = (Integer) request.getAttribute("userId");
    watchlistService.removeStockWatchlist(userId, stockTicker);
    return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
  }
}
