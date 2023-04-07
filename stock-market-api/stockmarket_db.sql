DROP DATABASE stockmarketdb;
DROP USER leonlow;
CREATE USER leonlow WITH PASSWORD 'iloveleonlow';
CREATE DATABASE stockmarketdb WITH template=template0 OWNER=leonlow;
\connect stockmarketdb;
ALTER DEFAULT privileges GRANT all ON tables TO leonlow;
ALTER DEFAULT privileges GRANT all ON sequences TO leonlow;

CREATE SEQUENCE accounts_seq INCREMENT 1 START 1;

CREATE TABLE accounts (
  user_id INTEGER DEFAULT nextval('accounts_seq') PRIMARY KEY,
  username VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  is_active INT NOT NULL,
  added_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

