package com.stockmarket.stockmarketapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import com.stockmarket.stockmarketapi.filters.AuthFilter;

@SpringBootApplication
public class StockMarketApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockMarketApiApplication.class, args);
		System.out.println("Server is listening on port 8080!");
	}

	@Bean
	public FilterRegistrationBean<AuthFilter> FilterResgistrationBean() {
		FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
		AuthFilter authFilter = new AuthFilter();
		registrationBean.setFilter(authFilter);
		registrationBean.addUrlPatterns("/api/*");
		// registrationBean.addUrlPatterns("/yahoo-api/*");
		return registrationBean;
	}

}
