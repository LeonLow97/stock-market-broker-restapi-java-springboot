# Stock Market REST API

## Project Specifications

A Stock Market Broker REST API is a web-based project that provides a set of endpoints for accessing and manipulating stock market data, specifically for stock market broker. The API allows for retrieving information about stocks, creating and canceling orders, and managing account information through a set of HTTP endpoints, using standard methods such as GET, POST, PUT and DELETE.

## User Management Endpoints

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | /users | Create a new user account |
| PUT | /users/{user_id} | Update an existing user account |
| DELETE | /users/{user_id} | Delete an existing user account |
| GET | /users/{user_id} | Retrieve information about a specific user account |
| GET | /users | Retrieve a list of all user accounts |
| POST | /authenticate | Authenticate a user and retrieve an access token |
| POST | /refresh-token | Refresh an access token |

## Stock Market Endpoints

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | /stocks | Retrieve a list of all stocks available in the stock market |
| GET | /stocks/{symbol} | Retrieve information about a specific stock, identified by its symbol |
| GET | /account | Retrieve information about the current user's account |
| POST | /orders | Create a new order to buy or sell a stock |
| PUT | /orders/{order_id} | Update an existing order |
| DELETE | /orders/{order_id} | Cancel an existing order |
| GET | /portfolio | Retrieve information about the current user's portfolio of stocks |
| GET | /transactions | Retrieve a list of all transactions made by the current user |
| GET | /transactions/{transaction_id} | Retrieve information about a specific transaction, identified by its ID |
| GET | /watchlist | Retrieve a list of all stocks in the current user's watchlist |
| POST | /watchlist | Add a stock to the current user's watchlist |
| DELETE | /watchlist/{symbol} | Remove a stock from the current user's watchlist |
| GET | /market-data | Retrieve market data, such as stock prices, indices and trading volumes |




