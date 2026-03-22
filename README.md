# Product Inventory API

RESTful microservice for managing a Product Inventory System built with Spring Boot 4, Spring Data JPA, and Hibernate.

## Tech Stack

- **Java 21**
- **Spring Boot 4.0.3**
- **Spring Data JPA** with Hibernate
- **H2 Database** (development) / **PostgreSQL 16** (Docker)
- **ModelMapper** for DTO mapping
- **Lombok** for boilerplate reduction
- **SpringDoc OpenAPI** (Swagger UI)
- **JUnit 5 + Mockito** for testing
- **Docker + Docker Compose** for containerization
- **JPA Specification** for dynamic query filtering

## Features

- Full CRUD operations with soft delete (DISCONTINUED status)
- Dynamic filtering with JPA Specification (category, price range, status)
- Pagination support on list endpoints
- Stock management with ADD/SUBTRACT operations
- Inventory statistics (active products, total value, average price by category)
- Input validation with Bean Validation
- Global exception handling with `@ControllerAdvice`
- Swagger UI for API documentation and testing
- Seed data for immediate testing

## API Endpoints

### CRUD Operations

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/products` | Create a new product |
| GET | `/api/v1/products/{id}` | Get product by ID |
| GET | `/api/v1/products` | List products with filters |
| PUT | `/api/v1/products/{id}` | Update a product |
| DELETE | `/api/v1/products/{id}` | Soft delete (DISCONTINUED) |

### Query Parameters for GET /products

| Parameter | Type | Description |
|-----------|------|-------------|
| category | String | Filter by category |
| priceMin | BigDecimal | Minimum price |
| priceMax | BigDecimal | Maximum price |
| status | String | ACTIVE or DISCONTINUED |
| page | int | Page number (default: 0) |
| size | int | Page size (default: 10) |

### Stock & Stats

| Method | Endpoint | Description |
|--------|----------|-------------|
| PATCH | `/api/v1/products/{id}/stock` | Update stock (ADD/SUBTRACT) |
| GET | `/api/v1/products/stats` | Inventory statistics |
| GET | `/api/v1/products/low-stock?threshold=10` | Products below stock threshold |

## Prerequisites

- Java 21
- Maven
- Git
- Docker (optional, for PostgreSQL)

## How to Run

### Option 1: Local with H2 (Quick Start)

```bash
git clone https://github.com/jarzate039-sketch/product-inventory-api.git
cd product-inventory-api
mvn spring-boot:run
```

Application starts at: `http://localhost:8080/api/v1/`
Swagger UI: `http://localhost:8080/api/v1/swagger-ui.html`
H2 Console: `http://localhost:8080/api/v1/h2-console` (URL: `jdbc:h2:mem:inventorydb`)

### Option 2: Docker with PostgreSQL

```bash
git clone https://github.com/jarzate039-sketch/product-inventory-api.git
cd product-inventory-api
docker-compose up --build
```

Application starts at: `http://localhost:8080/api/v1/`
Swagger UI: `http://localhost:8080/api/v1/swagger-ui.html`

To stop: `docker-compose down`
To stop and remove data: `docker-compose down -v`

## Running Tests

```bash
mvn test
```

Unit tests validate the Service layer using Mockito, covering:
- CRUD operations (create, read, update, soft delete)
- Business rule validations (duplicate products, discontinued updates, insufficient stock)

## Business Rules

1. **Unique product names** within the same category
2. **Soft delete** — sets status to DISCONTINUED, never removes from database
3. **Stock cannot go below 0** — returns error if subtraction would result in negative
4. **No price updates** on DISCONTINUED products
5. **Stats** calculated only from ACTIVE products

## Design Decisions

- **JPA Specification** for dynamic filtering instead of multiple repository methods — scales easily when new filters are needed
- **Soft delete** over hard delete to maintain data history and audit trail
- **DTO pattern** with separate Request/Response objects to decouple API contract from entity
- **Service interface + implementation** to allow easy mocking in tests and potential multiple implementations
- **Global exception handler** with custom exceptions for consistent error responses across all endpoints
- **Multi-stage Docker build** for smaller production images (JRE only, no JDK)
- **H2 for development, PostgreSQL for Docker** — fast local iteration with production-like containerized environment

## Project Structure

```
src/main/java/com/hycorp/inventorySystem/
├── config/                  # Configuration classes (ModelMapper)
├── constants/               # Enums (StatusEnum, StockOperationEnum)
├── controller/              # REST Controllers
├── dto/
│   ├── request/             # Request DTOs (ProductRequest, StockRequest)
│   └── response/            # Response DTOs (Product, Stock, Stats, Error)
├── entity/                  # JPA Entities
├── exceptions/              # Custom exceptions + GlobalExceptionHandler
├── repository/              # Spring Data JPA Repositories
├── service/
│   └── impl/                # Service interface + implementation
└── specification/           # JPA Specification for dynamic queries
```
