package com.stockmarket.stockmarketapi;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.stockmarket.stockmarketapi.service.UserService;
import com.stockmarket.stockmarketapi.web.UserController;

@SpringBootTest
class StockMarketApiApplicationTests {

	@Autowired
	private UserController userController;

	@Autowired
	UserService userService;

	@Test
	void contextLoads() {
		assertNotNull(userController);
		assertNotNull(userService);
	}

}
