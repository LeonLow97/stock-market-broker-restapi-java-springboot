package com.stockmarket.stockmarketapi.exception;

public class OrderNotFilledException extends RuntimeException {

  public OrderNotFilledException(String message) {
    super(message);
  }
  
}
