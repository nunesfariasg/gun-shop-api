#  Gun Shop API

REST API built with **Java** and **Spring Boot** for managing a specialized firearms, ammunition, and accessories store, including inventory, customer registration, and sales operations.

This project was developed as a coursework deliverable applying software analysis, relational database modeling, layered architecture, input validation, and business rule enforcement.

---

##  Tech Stack

* **Language:** Java 17+
* **Framework:** Spring Boot 3+
* **Persistence:** Spring Data JPA / Hibernate
* **Database:** PostgreSQL
* **Validation:** Jakarta Bean Validation (`@NotNull`, `@NotBlank`, `@Positive`, `@Email`, etc.)
* **Build Tool:** Maven

---

##  Project Architecture

The application follows a **layered architecture** to ensure separation of concerns, maintainability, and testability:

```text
src/main/java/com/gunshop/api/
├── controller/   # REST endpoints, request handling, and HTTP response status codes
├── service/      # Business rules, domain validations, and transaction orchestration
├── repository/   # Data access interfaces (Spring Data JPA)
├── model/        # JPA entities mapped to PostgreSQL tables
├── dto/          # Data Transfer Objects (Request/Response payloads)
└── exception/    # Global exception handling (@RestControllerAdvice)
```
---

## Resources (Entities) & Endpoints

The API provides full CRUD operations for the core domain resources:

###  Customers (/customers or /clientes)

Manages registered buyers and their licensing/documentation records.

    GET /customers — List all registered customers.

    GET /customers/{id} — Retrieve a specific customer by ID.

    POST /customers — Register a new customer.

    PUT /customers/{id} — Update an existing customer's details.

    DELETE /customers/{id} — Delete a customer from the system.


### Products / Firearms (/products or /produtos)

Catalog of firearms, ammunition, and accessories available for sale.

    GET /products — List all products.

    GET /products/{id} — Retrieve product details by ID.

    POST /products — Add a new product to the catalog.

    PUT /products/{id} — Update product information or stock levels.

    DELETE /products/{id} — Remove a product.


### Categories / Manufacturers (/categories or /categorias)

Product classification (e.g., Handguns, Rifles, Ammunition, Optics) or manufacturer management.

    GET /categories — List all categories.

    GET /categories/{id} — Retrieve a category by ID.

    POST /categories — Create a new category.

    PUT /categories/{id} — Update an existing category.

    DELETE /categories/{id} — Delete a category.


### Sales / Orders (/sales or /vendas)

Records commercial transactions linking customers, purchased items, and calculated totals.

    GET /sales — List all recorded sales.

    GET /sales/{id} — Retrieve details of a specific sale.

    POST /sales — Register a new sale.

    PUT /sales/{id} — Update sale status or details.

    DELETE /sales/{id} — Cancel/delete a sale.
---

## Business Rules Implemented

Beyond standard CRUD operations, the Service layer enforces the following domain rules:

    Stock Validation & Automatic Deduction: Prevents completing a sale if the requested item quantity exceeds available inventory. Upon a valid sale, product stock is automatically decremented.

    Server-Side Total Calculation: The total order value is dynamically calculated on the server using current unit prices and requested quantities, preventing client-side price tampering.

    Document & Record Uniqueness: Blocks duplicate customer registrations sharing the same tax ID or firearm license/registration number, returning a 409 Conflict response.

 Error Handling & Validation

    Bean Validation: All incoming DTOs are validated using Jakarta annotations (@NotBlank, @NotNull, @Positive, @Email, etc.).

    Global Exception Handler: Standardized JSON error responses mapped to semantic HTTP status codes:

        400 Bad Request — Input validation failures or business rule violations (e.g., insufficient stock).

        404 Not Found — Requested resource ID does not exist in the database.

        409 Conflict — Duplicate record conflict (e.g., existing registration number or serial number).

---

## Getting Started
Prerequisites

    Java 17 or higher

    Maven

    PostgreSQL running locally or via Docker

Setup & Run

1. Clone the repository:

```Bash
    git clone [https://github.com/nunesfariasg/gun-shop-api.git](https://github.com/nunesfariasg/gun-shop-api.git)
    cd gun-shop-api
```
   
2. Create the PostgreSQL database:
    SQL
```text
    CREATE DATABASE gun_shop_db;
```

3. Configure database credentials in application.properties (or application.yml):
   Edit src/main/resources/application.properties:
```text
    Properties

    spring.datasource.url=jdbc:postgresql://localhost:5432/gun_shop_db
    spring.datasource.username=your_username
    spring.datasource.password=your_password

    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
```

4. Run the application:

```Bash
    ./mvnw spring-boot:run
```
5. Access the API:

    The server will start at http://localhost:8080.

---

## Sample Requests (JSON)
Create Product (POST /products)

```JSON
{
"name": "G17 Gen5 Pistol",
"caliber": "9mm",
"price": 6500.00,
"stockQuantity": 10,
"categoryId": 1
}
```

Register Sale (POST /sales)
```JSON
{
  "customerId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

---

## Authors

    Nunes Farias — @nunesfariasg
    Luiz Otávio
    Matheus Buratto