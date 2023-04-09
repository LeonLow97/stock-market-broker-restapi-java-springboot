DROP DATABASE stockmarketdb;
DROP USER leonlow;
CREATE USER leonlow WITH PASSWORD 'iloveleonlow';
CREATE DATABASE stockmarketdb WITH template=template0 OWNER=leonlow;
\connect stockmarketdb;
ALTER DEFAULT privileges GRANT all ON tables TO leonlow;
ALTER DEFAULT privileges GRANT all ON sequences TO leonlow;

CREATE SEQUENCE accounts_seq START 1 INCREMENT 1;

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

-- SELECT setval('accounts_seq', (SELECT MAX(user_id) FROM accounts)+1);

CREATE TABLE orders (
  order_id SERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  stock_ticker VARCHAR(20) NOT NULL,
  order_type VARCHAR(10) NOT NULL,
  no_of_shares INT NOT NULL,
  price DECIMAL(15,2) NOT NULL,
  order_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES accounts (user_id)
);