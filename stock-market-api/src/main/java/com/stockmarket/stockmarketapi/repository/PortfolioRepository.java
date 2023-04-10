package com.stockmarket.stockmarketapi.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.stockmarket.stockmarketapi.entity.Portfolio;
import com.stockmarket.stockmarketapi.exception.ResourceNotFoundException;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
  
  Portfolio findByUserIdAndStockTicker(Long userId, String stockTicker) throws ResourceNotFoundException;
  List<Portfolio> findAllByUserId(Long userId) throws ResourceNotFoundException;
  Portfolio findByUserIdAndPortfolioId(Long userId, Long portfolioId) throws ResourceNotFoundException;

}
