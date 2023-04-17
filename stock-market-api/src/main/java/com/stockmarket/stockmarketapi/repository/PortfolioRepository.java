package com.stockmarket.stockmarketapi.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.stockmarket.stockmarketapi.entity.Portfolio;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

  Portfolio findByUserIdAndStockTicker(long userId, String stockTicker);

  List<Portfolio> findAllByUserId(long userId);

  Portfolio findByUserIdAndPortfolioId(long userId, long portfolioId);

}
