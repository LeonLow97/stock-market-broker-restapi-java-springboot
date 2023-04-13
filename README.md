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

- Stock orders are placed _before the market opens_ and will only be filled if the limit order price is within the low and high price after the market opens. Thus, the application is unable to fetch real-time per minute data as we are fetching the data from YahooFinanceAPI day highs and lows.
- **Purchasing** a stock:
  - If the purchase price of the stock is _greater_ than the day high, the price of the stock will be purchased at the day high value.
  - If the purchase price of the stock is _lower_ than the day low, the purchase limit order will not be filled.
  - If the purchase price of the stock is _within_ the day high and day low, the price of the stock will be purchased at the specified price.
- **Selling** a stock:
  - If the selling price of the stock is _greater_ than the day high, the selling limit order will not be filled.
  - If the selling price of the stock is _lower_ than the day low, the price of the stock will be sold at the day low value.
  - If the selling price of the stock is _within_ the day high and day low, the price of the stock will be sold at the specified price.

## Authentication Endpoints

| Method | Endpoint      | Description                                      |
| ------ | ------------- | ------------------------------------------------ |
| _POST_ | /api/register | Create a new user account                        |
| _POST_ | /api/login    | Authenticate a user and retrieve an access token |
| _GET_  | /api/user     | Get User Details by User Id.                     |

## Admin Role Endpoints (Low Priority)

| Method | Endpoint         | Description                                        |
| ------ | ---------------- | -------------------------------------------------- |
| PUT    | /users/{user_id} | Update an existing user account                    |
| DELETE | /users/{user_id} | Delete an existing user account                    |
| GET    | /users/{user_id} | Retrieve information about a specific user account |
| GET    | /users           | Retrieve a list of all user accounts               |

## Stock Market Endpoints

| Method   | Endpoint                     | Description                                                        |
| -------- | ---------------------------- | ------------------------------------------------------------------ |
| _POST_   | /api/deposit                 | Deposit money into the user's account.                             |
| _POST_   | /api/withdraw                | Withdraw money from the user's account.                            |
| _GET_    | /api/stocks/{stockTicker}    | Get details for a specific stock, identified by its ticker symbol  |
| _GET_    | /api/orders                  | Get the entire order history of the user.                          |
| _GET_    | /api/orders/{orderId}        | Get details for a specific order of the user.                      |
| _POST_   | /api/orders                  | Create a new order for a stock.                                    |
| _GET_    | /api/portfolio/              | Retrieve information about the current user's portfolio of stocks. |
| _GET_    | /api/portfolio/{portfolioId} | Retrieve information about a specified stock in the portfolio.     |
| _GET_    | /api/watchlist/{stockTicker} | Retrieve a list of all stocks in the current user's watchlist.     |
| _POST_   | /api/watchlist/{stockTicker} | Add a stock to the current user's watchlist.                       |
| _DELETE_ | /api/watchlist/{stockTicker} | Remove a stock from the current user's watchlist.                  |

## Swagger UI Documentation

- [Swagger UI](http://localhost:8080/swagger-ui/index.html)

## To Do

- Check if it is possible to buy a stock with an invalid ticker.
- For Deposit and Withdrawal, ensure that amount cannot be negative.
- Trim White Spaces
- Data Validation for all the fields
  - Accept INT, STRING, DOUBLE
  - Field length
- Fix added date and updated date to align with the action method because now it is hardcoded to be `LocalDateTime.now()`.
- Docker Compose
- **IMPORTANT**: Need to return generic exceptions in Spring Boot in the form of object for Swagger UI.