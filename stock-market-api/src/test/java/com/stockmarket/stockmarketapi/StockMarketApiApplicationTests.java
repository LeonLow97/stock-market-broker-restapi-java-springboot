package com.stockmarket.stockmarketapi;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.stockmarket.stockmarketapi.service.OrderService;
import com.stockmarket.stockmarketapi.service.PortfolioService;
import com.stockmarket.stockmarketapi.service.StockService;
import com.stockmarket.stockmarketapi.service.UserService;
import com.stockmarket.stockmarketapi.service.WatchlistService;
import com.stockmarket.stockmarketapi.web.OrderController;
import com.stockmarket.stockmarketapi.web.PortfolioController;
import com.stockmarket.stockmarketapi.web.StockController;
import com.stockmarket.stockmarketapi.web.UserController;
import com.stockmarket.stockmarketapi.web.WatchlistController;

@SpringBootTest
class StockMarketApiApplicationTests {

	@Autowired
	private UserController userController;
	@Autowired
	private OrderController orderController;
	@Autowired
	private StockController stockController;
	@Autowired
	private PortfolioController portfolioController;
	@Autowired
	private WatchlistController watchlistController;

	@Autowired
	UserService userService;
	@Autowired
	OrderService orderService;
	@Autowired
	StockService stockService;
	@Autowired
	PortfolioService portfolioService;
	@Autowired
	WatchlistService watchlistService;

	@Test
	void contextLoads() {
		assertNotNull(userController);
		assertNotNull(orderController);
		assertNotNull(stockController);
		assertNotNull(portfolioController);
		assertNotNull(watchlistController);

		assertNotNull(userService);
		assertNotNull(orderService);
		assertNotNull(stockService);
		assertNotNull(portfolioService);
		assertNotNull(watchlistService);
	}

}
