package com.stockmarket.stockmarketapi;

public class Constants {

  // Secret Key for Signing a Digital Signature for JWT Token
  public static final String API_SECRET_KEY = "YHgZnX9JGkRz8B6cLp4UyNqoDWjrSv7u";

  // JWT Token Expiry in Milliseconds, in this case it is 2 minutes
  public static final long TOKEN_VALIDITY = 2 * 60 * 60 * 1000;
  
}
