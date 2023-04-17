package com.stockmarket.stockmarketapi.repositorytests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.stockmarket.stockmarketapi.entity.Portfolio;
import com.stockmarket.stockmarketapi.repository.PortfolioRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PortfolioRepositoryTests {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Test
    void InjectedComponentsAreNotNull() {
        assertNotNull(portfolioRepository);
    }

    @Test
    public void Test_FindByUserIdAndStockTickerSuccess() {
        // Arrange
        Portfolio portfolio = new Portfolio(1L, "Google", "GOOGL", 20, 122.17, 108.23, 0.0, 0.0);
        portfolio.setPortfolioId(1L);
        portfolioRepository.save(portfolio);

        // Act
        Portfolio dbPortfolio = portfolioRepository.findByUserIdAndStockTicker(1L, "GOOGL");

        // Assert
        assertNotNull(dbPortfolio);
    }

    @Test
    public void Test_FindByUserIdAndStockTickerNotFound() {
        // Arrange
        Portfolio portfolio = new Portfolio(1L, "Google", "GOOGL", 20, 122.17, 108.23, 0.0, 0.0);
        portfolio.setPortfolioId(1L);
        portfolioRepository.save(portfolio);

        // Act
        Portfolio dbPortfolio = portfolioRepository.findByUserIdAndStockTicker(1L, "AAPL");

        // Assert
        assertNull(dbPortfolio);
    }

    @Test
    public void Test_FindAllByUserIdSuccess() {
        // Arrange
        portfolioRepository.deleteAll();
        Portfolio portfolio1 = new Portfolio(1L, "Google", "GOOGL", 20, 122.17, 108.23, 0.0, 0.0);
        Portfolio portfolio2 = new Portfolio(1L, "Apple", "AAPL", 20, 122.17, 108.23, 0.0, 0.0);
        Portfolio portfolio3 = new Portfolio(1L, "JP Morgan", "JPM", 20, 122.17, 108.23, 0.0, 0.0);
        portfolioRepository.save(portfolio1);
        portfolioRepository.save(portfolio2);
        portfolioRepository.save(portfolio3);

        // Act
        List<Portfolio> portfolioList = portfolioRepository.findAllByUserId(1L);

        // Assert
        assertEquals(3, portfolioList.size());
    }

    @Test
    public void Test_FindAllByUserIdNotFound() {
        // Act
        List<Portfolio> portfolioList = portfolioRepository.findAllByUserId(3L);

        // Assert
        assertTrue(portfolioList.isEmpty());
    }

    @Test
    public void Test_FindByUserIdAndPortfolioId() {
        // Arrange
        Portfolio testPortfolio = new Portfolio(1L, "Apple", "AAPL", 20, 122.17, 108.23, 0.0, 0.0);
        testPortfolio.setPortfolioId(1L);
        portfolioRepository.save(testPortfolio);

        // Act
        Portfolio dbPortfolio = portfolioRepository.findByUserIdAndPortfolioId(1L, 1L);

        // Assert
        assertEquals("AAPL", dbPortfolio.getStockTicker());
    }

    @Test
    public void Test_FindByUserIdAndPortfolioIdNotFound() {
        // Act
        Portfolio dbPortfolio = portfolioRepository.findByUserIdAndPortfolioId(1L, 10L);

        // Assert
        assertNull(dbPortfolio);
    }

}
