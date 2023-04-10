DROP DATABASE stockmarketdb;
DROP USER leonlow;
CREATE USER leonlow WITH PASSWORD 'iloveleonlow';
CREATE DATABASE stockmarketdb WITH template=template0 OWNER=leonlow;
\connect stockmarketdb;
ALTER DEFAULT privileges GRANT all ON tables TO leonlow;
ALTER DEFAULT privileges GRANT all ON sequences TO leonlow;

CREATE TABLE accounts (
  user_id SERIAL PRIMARY KEY,
  username VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  balance NUMERIC(15, 2) NOT NULL,
  is_active INT NOT NULL,
  added_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE orders (
  order_id SERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  stock_ticker VARCHAR(20) NOT NULL,
  order_type VARCHAR(10) NOT NULL,
  no_of_shares INT NOT NULL,
  cost DECIMAL(15,2) NOT NULL,
  order_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES accounts (user_id)
);

CREATE TABLE portfolio (
  portfolio_id SERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  stock_name VARCHAR(255) NOT NULL,
  stock_ticker VARCHAR(20) NOT NULL,
  no_of_shares INT NOT NULL,
  cost DECIMAL(15, 2) NOT NULL,
  price DECIMAL(15,2) NOT NULL,
  pnl_in_percentage DECIMAL(3, 2) NOT NULL,
  pnl_in_dollars DECIMAL(15, 2) NOT NULL,
  added_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES accounts(user_id)
);

CREATE TABLE watchlist (
  watchlist_id SERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  stock_name VARCHAR(255) NOT NULL,
  stock_ticker VARCHAR(20) NOT NULL,
  price DECIMAL(15, 2) NOT NULL,
  previous_day_close DECIMAL(15, 2) NOT NULL,
  _52_week_high DECIMAL(15, 2) NOT NULL,
  _52_week_low DECIMAL(15, 2) NOT NULL,
  market_cap_in_billions DECIMAL(15, 2) NOT NULL,
  annual_dividend_yield DECIMAL(4, 2) NOT NULL,
  updated_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES accounts(user_id)
);

