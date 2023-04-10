package com.stockmarket.stockmarketapi.service;

import java.util.List;
import com.stockmarket.stockmarketapi.entity.Portfolio;

public interface PorfolioService {
  
  List<Portfolio> getPortfolio(int userId);
  Portfolio getPortfolioStock(int userId, int portfolioId);

}
