package com.stockmarket.stockmarketapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.stockmarket.stockmarketapi.entity.Portfolio;
import com.stockmarket.stockmarketapi.exception.ResourceNotFoundException;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
  
  Portfolio findByUserIdAndStockTicker(Long userId, String stockTicker) throws ResourceNotFoundException;

}
