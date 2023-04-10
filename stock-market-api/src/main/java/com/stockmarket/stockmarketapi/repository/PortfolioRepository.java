package com.stockmarket.stockmarketapi.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.stockmarket.stockmarketapi.entity.Portfolio;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
  
  Portfolio findByUserIdAndStockTicker(Long userId, String stockTicker);
  List<Portfolio> findAllByUserId(Long userId);
  Portfolio findByUserIdAndPortfolioId(Long userId, Long portfolioId);

}
