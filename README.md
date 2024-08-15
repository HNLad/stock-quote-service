# Stock Quote Service

## Introduction
Welcome to the Stock Quote Service! This service is built to provide real-time stock market data along with robust user authentication using JWT (JSON Web Tokens). 
Below, I outline the problem, requirements, solution architecture, and the development process.

## Problem Statement
The challenge at hand is to create a backend service that:
- Retrieves real-time stock quotes from an external API (Alpha Vantage) and stores them in a database.
- Authenticates users and provides them with JWT tokens for secure access.

## Requirements

### Functional Requirements
1. **User Authentication:**
   - Enable users to log in with a username and password.
   - Provide a JWT token upon successful login for use in subsequent requests.

2. **Stock Quote Retrieval:**
   - Allow users to fetch stock quotes based on stock symbols.
   - Support batch requests for multiple stock quotes.
   - Return comprehensive stock data including symbol, price, change, percentage change, and timestamp.

### Non-Functional Requirements
- **Security:** Endpoints must be secured using JWT authentication.
- **Performance:** The service should efficiently manage multiple requests and provide quick responses.
- **Scalability:** The service architecture should support growth to handle increasing load demands.

## Service Architecture

### Tech Stack & Tools
- **Java**
- **Spring Boot**
- **PostgreSQL**
- **JWT**

### High-Level Architecture
- **Configuration:** Key-values are configured in `application.properties`.
- **Database:** Utilizes HikariCP for PostgreSQL database connection pooling.
- **Authentication Service:** Manages user login and JWT token generation.
- **Stock Quote Service:** Fetches stock data by communicating with an external stock API.

### Components
- **Controller:**
  - `LogInController` handles user login requests.
  - `StockQuoteController` handles requests for stock quotes.
- **Service:** `StockQuoteServiceImpl` interacts with the external stock API.
- **Repository:** `StockQuoteRepoImpl` manages interactions with the database.
- **Security:**
  - `JwtRequestFilter` is responsible for filtering and validating JWT tokens.
  - `JwtUtil` is used for generating and validating JWT tokens.
  - `StaticUserDetails` handles the loading of user details.
- **Logging:** The logger configuration file manages log settings to ensure smooth operation.
