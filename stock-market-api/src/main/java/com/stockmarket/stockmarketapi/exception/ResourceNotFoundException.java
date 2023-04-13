package com.stockmarket.stockmarketapi.exception;

public class ResourceNotFoundException extends RuntimeException {
  
  public ResourceNotFoundException(String message) {
    super(message);
  }

}
