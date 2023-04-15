package com.stockmarket.stockmarketapi.service;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stockmarket.stockmarketapi.entity.Portfolio;
import com.stockmarket.stockmarketapi.exception.ResourceNotFoundException;
import com.stockmarket.stockmarketapi.repository.PortfolioRepository;

@Service
@Transactional
public class PortfolioServiceImpl implements PortfolioService {

  @Autowired
  PortfolioRepository portfolioRepository;

  @Override
  public List<Portfolio> getPortfolio(int userId) {
    List<Portfolio> portfolio = portfolioRepository.findAllByUserId(Long.valueOf(userId));
    if (portfolio.isEmpty()) {
      throw new ResourceNotFoundException("Portfolio does not exist.");
    }
    return portfolio;
  }

  @Override
  public Portfolio getPortfolioStock(int userId, int portfolioId) {
    Portfolio portfolio = portfolioRepository.findByUserIdAndPortfolioId(Long.valueOf(userId), Long.valueOf(portfolioId));
    if (portfolio == null) {
      throw new ResourceNotFoundException("Stock does not exist in portfolio");
    }
    return portfolio;
  }
  
}
