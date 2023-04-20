package com.stockmarket.stockmarketapi.controllers;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.stockmarket.stockmarketapi.service.StockService;
import com.stockmarket.stockmarketapi.stockmodel.StockData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Stock Controller", description = "Connecting to Yahoo Finance API to retrieve real time daily stock data.")
@RestController
@RequestMapping("/yahoo-api")
public class StockController {

  @Autowired
  StockService stockService;

  @Operation(summary = "Get stock data", description = "Retrieves the price of the stock and other fundamental details of the business.")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Successfully retrieved stock details from Yahoo Finance API", content = @Content(schema = @Schema(implementation = StockData.class)))
  })
  @GetMapping(value = "/stocks/{stockTicker}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<StockData> getStock(@PathVariable("stockTicker") String stockTicker) {
    try {
      StockData stockData = stockService.findStock(stockTicker);
      return new ResponseEntity<>(stockData, HttpStatus.OK);
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

}
