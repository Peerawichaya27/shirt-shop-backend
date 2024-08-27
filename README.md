# Shirt Shop Backend

## Overview
This project is the backend service for the Shirt Shop application, handling all data management and business logic. It provides APIs for user authentication, product management, order processing, and more.

## Technologies Used
- **Java**: Core programming language used for backend development.
- **Spring Boot**: Framework for creating stand-alone, production-grade Spring based applications easily.
- **Maven**: Dependency management and build automation tool.
- **PostgreSQL**: Database for storing all persistent data.
- **JWT (JSON Web Tokens)**: Used for securely transmitting information between parties as a JSON object.
- **Docker**: Containerization platform used to package and run the application in a predictable manner.
- **Postman**: Utilized for creating and using a collection to test API endpoints, ensuring the backend's functionality and reliability.
- **Makefile**: Used to automate the setup, building, and management of the project, enhancing the development process.

## Project Structure
- `src/main/java/com/shirtshop/shirtshop` - Main application package.
  - `/controllers` - REST controllers to handle HTTP requests.
  - `/models` - Entity models corresponding to database tables.
  - `/repositories` - Spring Data JPA repositories for database access.
  - `/services` - Service classes with business logic.
  - `/exception` - Custom exceptions and error handling classes.
- `src/main/resources` - Contains application configurations and static resources.

## Setup and Installation
1. **Clone the repository:**

```
git clone https://github.com/Peerawichaya27/shirt-shop-backend.git 
```

2. **Navigate to the Directory Containing the Makefile:**

```
cd shirt-shop-backend
```

3. **Build and Run the Application Using Makefile:**
- Build the application:
  ```
  make build
  ```
- Start the application:
  ```
  make run
  ```

4. **Database Management:**
- Start the PostgreSQL database using Docker:
  ```
  make postgres
  ```
- Create the database:
  ```
  make createdb
  ```
- Drop the database if needed:
  ```
  make dropdb
  ```
- Stop the PostgreSQL container:
  ```
  make stopdb
  ```
- Remove the PostgreSQL container:
  ```
  make removedb
  ```

5. **Clean Up:**
- Clean the Maven build and Docker containers:
  ```
  make clean
  ```

## API Endpoints

#### Customer APIs
- **POST /auth/customer/signup**: Registers a new customer.
- **POST /auth/login**: Authenticates a user and returns an access token.
- **GET /customer/current**: Retrieves current authenticated customer's details.
- **PUT /customer/update/password**: Updates a customer's password.
- **PUT /customer/update/address**: Updates a customer's address.
- **PUT /customer/update/card**: Updates a customer's credit card information.
- **DELETE /customer/delete/address**: Deletes an address from a customer's account.

#### Seller APIs
- **POST /auth/seller/signup**: Registers a new seller.
- **GET /sellers**: Lists all registered sellers.
- **GET /sellers/current**: Retrieves current authenticated seller's details.
- **PUT /sellers/update/password**: Updates a seller's password.

#### Product APIs
- **POST /products**: Adds a new product.
- **GET /product/{id}**: Retrieves a specific product by ID.
- **PUT /products**: Updates product details.
- **DELETE /product/{id}**: Removes a product.
- **GET /products**: Lists all products.
- **GET /products/seller/{sellerId}**: Retrieves all products by a specific seller.
- **GET /products/{category}**: Retrieves products by category.

#### Cart APIs
- **POST /cart/add**: Adds a product to the cart.
- **GET /cart**: Retrieves all products in the cart.
- **DELETE /cart**: Removes a product from the cart.
- **DELETE /cart/clear**: Clears all products from the cart.

#### Order APIs
- **POST /order/add**: Places a new order.
- **GET /orders**: Retrieves all orders.
- **GET /orders/{orderId}**: Retrieves a specific order by ID.
- **DELETE /orders/{orderId}**: Cancels an order.
- **PUT /orders/{orderId}**: Updates an order.

## Security Measures

- **Authentication and Authorization:** 
  - Uses JWT (JSON Web Tokens) for secure, efficient user authentication and authorization.
  - Implements role-based access control (RBAC) to differentiate between seller and customer roles:
    - **Seller Role**: Sellers have exclusive access to endpoints related to managing their products, viewing their sales data, and other seller-specific functionalities.
    - **Customer Role**: Customers can access endpoints for viewing products, placing orders, managing their profiles, and other customer-centric interactions.
  - Access restrictions are enforced at both the controller and service levels to ensure that users can only interact with parts of the application that are relevant to their roles.

- **Data Validation:** 
  - Input validation is applied extensively across all endpoints to prevent SQL injection, cross-site scripting (XSS), and other security threats.

- **Secure Database Access:** 
  - Database connections are encrypted, and SQL queries are parameterized to enhance security and prevent unauthorized access.

- **Error Handling:** 
  - Implements robust error handling to manage exceptions and log errors efficiently without exposing sensitive information to the client.

## Using the Postman Collection

### Overview
The Postman collection (`shirtshop.postman_API-TEST.json`) enables you to test and interact with the API endpoints efficiently. It includes requests for all operations such as create, read, update, and delete across various services like customer, product, and order management.

### Importing and Using the Collection
1. **Importing**: Open Postman, go to `File` > `Import`, and upload the collection file.
2. **Using the Collection**:
   - **Set up**: After importing, set up environment variables in Postman for server URLs and user tokens to switch easily between different environments.
   - **Execution**: Select a request, ensure the right parameters are set, and hit `Send` to see the response.

This setup allows quick testing and debugging, facilitating a deeper understanding of the API's capabilities and response structure.



