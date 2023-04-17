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
import com.stockmarket.stockmarketapi.entity.Watchlist;
import com.stockmarket.stockmarketapi.repository.WatchlistRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WatchlistRepositoryTests {

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Test
    void InjectedComponentsAreNotNull() {
        assertNotNull(watchlistRepository);
    }

    @Test
    public void Test_FindAllByUserIdSuccess() {
        // Arrange
        watchlistRepository.deleteAll();
        Watchlist watchlist1 =
                new Watchlist(1L, "Google", "GOOGL", 102.34, 123.21, 123.11, 99.12, 123.1, 0.0);
        Watchlist watchlist2 =
                new Watchlist(1L, "Apple", "AAPL", 122.34, 123.21, 123.11, 99.12, 123.1, 0.0);
        Watchlist watchlist3 =
                new Watchlist(1L, "Tesla", "TSLA", 132.34, 123.21, 123.11, 99.12, 123.1, 0.0);
        watchlistRepository.save(watchlist1);
        watchlistRepository.save(watchlist2);
        watchlistRepository.save(watchlist3);

        // Act
        List<Watchlist> watchlist = watchlistRepository.findAllByUserId(1L);

        // Assert
        assertEquals(3, watchlist.size());
    }

    @Test
    public void Test_FindAllByUserIdNotFound() {
        // Act
        List<Watchlist> watchlist = watchlistRepository.findAllByUserId(5L);

        // Assert
        assertTrue(watchlist.isEmpty());
    }

    @Test
    public void Test_FindByUserIdAndStockTicker() {
        // Arrange
        Watchlist watchlist =
                new Watchlist(2L, "Alibaba", "BABA", 122.34, 123.21, 123.11, 99.12, 123.1, 0.0);
        watchlistRepository.save(watchlist);

        // Act
        Watchlist dbWatchlist = watchlistRepository.findByUserIdAndStockTicker(2L, "BABA");

        // Assert
        assertEquals(watchlist, dbWatchlist);
    }

    @Test
    public void Test_FindByUserIdAndStockTickerNotFound() {
        // Act
        Watchlist dbWatchlist = watchlistRepository.findByUserIdAndStockTicker(7L, "BABA");

        // Assert
        assertNull(dbWatchlist);
    }

    @Test
    public void Test_RemoveByUserIdAndStockTickerSuccess() {
        // Act
        watchlistRepository.removeByUserIdAndStockTicker(2L, "BABA");
        Watchlist dbWatchlist = watchlistRepository.findByUserIdAndStockTicker(2L, "BABA");

        // Assert
        assertNull(dbWatchlist);
    }

    @Test
    public void Test_RemoveByUserIdAndStockTickerNotFound() {
        // Arrange
        Watchlist watchlist =
                new Watchlist(3L, "Alibaba", "BABA", 122.34, 123.21, 123.11, 99.12, 123.1, 0.0);
        watchlistRepository.save(watchlist);

        // Act
        watchlistRepository.removeByUserIdAndStockTicker(42L, "BABA");
        Watchlist dbWatchlist = watchlistRepository.findByUserIdAndStockTicker(3L, "BABA");

        // Assert
        assertNotNull(dbWatchlist);
    }

}
