package com.stockmarket.stockmarketapi.controllers;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.stockmarket.stockmarketapi.entity.Portfolio;
import com.stockmarket.stockmarketapi.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Portfolio Controller",
    description = "To retrieve the portfolio of the user to view stocks that have been purchased.")
@RestController
@RequestMapping("/api")
public class PortfolioController {

  @Autowired
  PortfolioService portfolioService;

  @Operation(summary = "Get Portfolio",
      description = "Retrieves the entire portfolio of stocks of the user.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Successfully retrieved the portfolio of the user.",
          content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = Portfolio.class)))),
      @ApiResponse(responseCode = "404", description = "NOT FOUND - portfolio does not exist",
          content = @Content)})
  @GetMapping(value = "/portfolio", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Portfolio>> getPortfolio(HttpServletRequest request) {
    Integer userId = (Integer) request.getAttribute("userId");
    List<Portfolio> portfolio = portfolioService.getPortfolio(userId);
    return new ResponseEntity<>(portfolio, HttpStatus.OK);
  }

  @Operation(summary = "Get Portfolio Stock",
      description = "Retrieves the details of a particular stock in the portfolio of the user.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Successfully retrieved the details of the stock of the user.",
          content = @Content(schema = @Schema(implementation = Portfolio.class))),
      @ApiResponse(responseCode = "404",
          description = "NOT FOUND - stock does not exist in user's portfolio.",
          content = @Content)})
  @GetMapping(value = "/portfolio/{portfolioId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Portfolio> getPortfolioStock(HttpServletRequest request,
      @PathVariable("portfolioId") Integer portfolioId) {
    Integer userId = (Integer) request.getAttribute("userId");
    Portfolio portfolio = portfolioService.getPortfolioStock(userId, portfolioId);
    return new ResponseEntity<>(portfolio, HttpStatus.OK);
  }

}
