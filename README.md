# Stock Market REST API

## Project Specifications

A Stock Market Broker REST API is a web-based project that provides a set of endpoints for accessing and manipulating stock market data, specifically for stock market broker. The API allows for retrieving information about stocks, creating and canceling orders, and managing account information through a set of HTTP endpoints, using standard methods such as GET, POST, PUT and DELETE.

## Learning Outcomes

- JUnit Testing
- Spring Security: Token-Based Authentication (JWT Token)
- H2 and JPA: For SQL Database Simulation
  - In reality, we will use a production-based relational database such as PostgreSQL and MySQL.
- API Documentation: OpenAPI and Swagger
- Dependency Injection: Using `@Autowired`, interfaces and implementations.
- Error Handling
- 3-Tier Architecture: Repository, Service and Web.

## Authentication Endpoints

| Method | Endpoint | Description                                      |
| ------ | -------- | ------------------------------------------------ |
| POST   | /signup  | Create a new user account                        |
| POST   | /login   | Authenticate a user and retrieve an access token |

## Admin Role Endpoints

| Method | Endpoint | Description                                      |
| ------ | -------- | ------------------------------------------------ |
| PUT | /users/{user_id} | Update an existing user account |
| DELETE | /users/{user_id} | Delete an existing user account |
| GET | /users/{user_id} | Retrieve information about a specific user account |
| GET | /users | Retrieve a list of all user accounts |

## Stock Market Endpoints

| Method | Endpoint                       | Description                                                                  |
| ------ | ------------------------------ | ---------------------------------------------------------------------------- |
| GET    | /stocks                        | Retrieve a list of all stocks available in the stock market                  |
| GET    | /stocks/{symbol}               | Retrieve information about a specific stock, identified by its ticker symbol |
| POST   | /orders                        | Create a new order to buy or sell a stock                                    |
| PUT    | /orders/{order_id}             | Update an existing order                                                     |
| DELETE | /orders/{order_id}             | Cancel an existing order                                                     |
| GET    | /portfolio/{user_id}           | Retrieve information about the current user's portfolio of stocks            |
| GET    | /transactions                  | Retrieve a list of all transactions made by the current user                 |
| GET    | /transactions/{transaction_id} | Retrieve information about a specific transaction, identified by its ID      |
| GET    | /watchlist/{user_id}           | Retrieve a list of all stocks in the current user's watchlist                |
| POST   | /watchlist/{user_id}           | Add a stock to the current user's watchlist                                  |
| DELETE | /watchlist/{symbol}            | Remove a stock from the current user's watchlist                             |
| GET    | /market-data                   | Retrieve market data, such as stock prices, indices and trading volumes      |
