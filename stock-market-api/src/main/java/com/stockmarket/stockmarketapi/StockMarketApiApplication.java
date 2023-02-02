package com.stockmarket.stockmarketapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockMarketApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockMarketApiApplication.class, args);
		System.out.println("Server is listening on port 8080!");
	}

}
