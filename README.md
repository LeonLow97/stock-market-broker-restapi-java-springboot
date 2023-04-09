# Stock Market REST API

## Project Specifications

A Stock Market Broker REST API is a web-based project that provides a set of endpoints for accessing and manipulating stock market data, specifically for stock market broker. The API allows for retrieving information about stocks, creating and canceling orders, and managing account information through a set of HTTP endpoints, using standard methods such as GET, POST, PUT and DELETE. When a new user signs up for an account using this Stock Market Broker, $1000.0 will be credited into the user's account as a token of appreciation.

## Learning Outcomes

- JUnit Testing
- Spring Security: Token-Based Authentication (JWT Token)
- JPA: For Object-Relational Mapping (ORM) of PostgreSQL.
- API Documentation: OpenAPI and Swagger
- Dependency Injection: Using `@Autowired`, interfaces and implementations.
- Error Handling
- 3-Tier Architecture: Repository, Service and Web.

## Assumptions

- Stock orders are placed *before the market opens* and will only be filled if the limit order price is within the low and high price after the market opens.

## Authentication Endpoints

| Method | Endpoint      | Description                                      |
| ------ | ------------- | ------------------------------------------------ |
| POST   | /api/register | Create a new user account                        |
| POST   | /api/login    | Authenticate a user and retrieve an access token |

## Admin Role Endpoints (Low Priority)

| Method | Endpoint         | Description                                        |
| ------ | ---------------- | -------------------------------------------------- |
| PUT    | /users/{user_id} | Update an existing user account                    |
| DELETE | /users/{user_id} | Delete an existing user account                    |
| GET    | /users/{user_id} | Retrieve information about a specific user account |
| GET    | /users           | Retrieve a list of all user accounts               |

## Stock Market Endpoints

| Method | Endpoint                           | Description                                                             |
| ------ | ---------------------------------- | ----------------------------------------------------------------------- |
| POST   | /api/deposit                       | Deposit money into the user's account.                                  |
| POST   | /api/withdraw                      | Withdraw money from the user's account.                                 |
| GET    | /api/stocks/{stockTicker}          | Get details for a specific stock, identified by its ticker symbol       |
| GET    | /api/orders                        | Get the entire order history of the user.                               |
| GET    | /api/orders/{orderId}              | Get details for a specific order of the user.                           |
| POST   | /api/orders                        | Create a new order for a stock.                                         |
| GET    | /portfolio/{userId}                | Retrieve information about the current user's portfolio of stocks       |
| GET    | /api/portfolio/{userId}/history    | Get the historical portfolio for the user.                              |
| GET    | /api/transactions/{userId}         | Retrieve a list of all transactions made by the current user            |
| GET    | /api/transactions/{transaction_id} | Retrieve information about a specific transaction, identified by its ID |
| GET    | /api/watchlist/{user_id}           | Retrieve a list of all stocks in the current user's watchlist           |
| POST   | /api/watchlist/{user_id}           | Add a stock to the current user's watchlist                             |
| DELETE | /api/watchlist/{symbol}            | Remove a stock from the current user's watchlist                        |
| GET    | /api/market-data                   | Retrieve market data, such as stock prices, indices and trading volumes |

## To Do

- For Deposit and Withdrawal, ensure that amount cannot be negative.
- Trim White Spaces
- Data Validation for all the fields
  - Accept INT, STRING, DOUBLE
  - Field length