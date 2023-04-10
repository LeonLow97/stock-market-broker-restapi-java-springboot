package com.stockmarket.stockmarketapi.web;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.stockmarket.stockmarketapi.entity.Portfolio;
import com.stockmarket.stockmarketapi.service.PorfolioService;

@RestController
@RequestMapping("/api")
public class PortfolioController {

  @Autowired
  PorfolioService portfolioService;

  @GetMapping("/portfolio")
  public ResponseEntity<List<Portfolio>> getPortfolio(HttpServletRequest request) {
    int userId = (Integer) request.getAttribute("userId");
    List<Portfolio> portfolio = portfolioService.getPortfolio(userId);
    return new ResponseEntity<>(portfolio, HttpStatus.OK);
  }

  @GetMapping("/portfolio/{portfolioId}")
  public ResponseEntity<Portfolio> getPortfolioStock(HttpServletRequest request,
      @PathVariable("portfolioId") Integer portfolioId) {
    int userId = (Integer) request.getAttribute("userId");
    Portfolio portfolio = portfolioService.getPortfolioStock(userId, portfolioId);
    return new ResponseEntity<>(portfolio, HttpStatus.OK);
  }

}
