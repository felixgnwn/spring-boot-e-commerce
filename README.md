<img width="1085" height="855" alt="image" src="https://github.com/user-attachments/assets/fe9df470-0ad5-4d1f-be1c-1040f27d1566" /># 🛒 E-Commerce Backend API

A RESTful e-commerce backend built with **Spring Boot**, **PostgreSQL**, and **Java 21**. Features user registration, product management, a cart system, and order processing.

---

## 🧰 Tech Stack

| Technology | Version |
|---|---|
| Java | 21 |
| Spring Boot | 4.0.3 |
| Spring Web | - |
| Spring Data JPA | - |
| PostgreSQL | Latest |
| Hibernate | - |
| Lombok | - |
| Maven | - |
| Docker | - |

---

## 🏗️ Project Architecture

```
src/main/java/com/felixgnwn/
├── ECommerceApplication.java
├── controller/
│   ├── ProductController.java
│   ├── UserController.java
│   ├── CartController.java
│   └── OrderController.java
├── service/
│   ├── ProductService.java
│   ├── UserService.java
│   ├── CartService.java
│   └── OrderService.java
├── repository/
│   ├── ProductRepository.java
│   ├── UserRepository.java
│   ├── CartRepository.java
│   ├── CartItemRepository.java
│   └── OrderRepository.java
├── entity/
│   ├── Product.java
│   ├── User.java
│   ├── Cart.java
│   ├── CartItem.java
│   ├── Order.java
│   ├── OrderItem.java
│   └── OrderStatus.java
└── dto/
    ├── ProductRequest.java
    ├── ProductResponse.java
    ├── UserRequest.java
    ├── UserResponse.java
    ├── CartItemRequest.java
    ├── CartItemResponse.java
    ├── CartResponse.java
    ├── OrderItemResponse.java
    └── OrderResponse.java
```

---

## ⚙️ Getting Started

### Prerequisites

- Java 21
- Maven
- Docker & Docker Compose

### 1. Clone the repository

```bash
git clone https://github.com/felixgnwn/spring-boot-e-commerce.git
cd spring-boot-e-commerce
```

### 2. Start PostgreSQL with Docker

```bash
docker-compose up -d
```

This starts a PostgreSQL container with the following config:

```
Host:     localhost
Port:     5332
Database: ecommercedb
Username: felixgnwn
Password: password
```

### 3. Run the application

```bash
./mvnw spring-boot:run
```

The server starts at `http://localhost:8080`

---

## 🐳 Docker Compose

```yaml
services:
  db:
    container_name: postgres-felixgnwn-ecommerce
    image: postgres:latest
    environment:
      POSTGRES_USER: felixgnwn
      POSTGRES_PASSWORD: password
      PGDATA: /data/postgres
    volumes:
      - db:/data/postgres
    ports:
      - "5332:5432"
    networks:
      - db
    restart: unless-stopped

networks:
  db:
    driver: bridge
volumes:
  db:
```

---

## 📡 API Endpoints

### 👤 Users

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/users/register` | Register a new user |
| `GET` | `/api/users` | Get all users |
| `GET` | `/api/users/{id}` | Get user by ID |

### 📦 Products

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/products` | Get all products |
| `GET` | `/api/products/{id}` | Get product by ID |
| `GET` | `/api/products/search?name=` | Search products by name |
| `POST` | `/api/products` | Create a product |
| `PUT` | `/api/products/{id}` | Update a product |
| `DELETE` | `/api/products/{id}` | Delete a product |

### 🛒 Cart

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/cart/{userId}` | Get cart for user |
| `POST` | `/api/cart/{userId}/add` | Add item to cart |
| `DELETE` | `/api/cart/{userId}/remove/{productId}` | Remove item from cart |
| `DELETE` | `/api/cart/{userId}/clear` | Clear entire cart |

### 📋 Orders

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/orders/{userId}/place` | Place order from cart |
| `GET` | `/api/orders/user/{userId}` | Get all orders for user |
| `GET` | `/api/orders/{orderId}` | Get order by ID |
| `PATCH` | `/api/orders/{orderId}/status?status=` | Update order status |

**Order Status values:** `PENDING` `CONFIRMED` `SHIPPED` `DELIVERED` `CANCELLED`

---

## 🧪 Testing with Postman

### Happy Path Flow

```
Register User → Create Product → Add to Cart → Place Order → Update Status
```

---

### Step 1 — Register a User

**POST** `http://localhost:8080/api/users/register`

```json
{
    "name": "Felix",
    "email": "felix@example.com",
    "password": "password123"
}
```

<img width="1082" height="721" alt="image" src="https://github.com/user-attachments/assets/5c5317bb-2b99-4183-938c-cc2a28978850" />

---

### Step 2 — Create a Product

**POST** `http://localhost:8080/api/products`

```json
{
    "name": "Nike Air Max",
    "description": "Running shoes",
    "price": 150.00,
    "stock": 50
}
```

<img width="1086" height="773" alt="image" src="https://github.com/user-attachments/assets/4a71b09e-c0be-4bc5-96cd-ccdcb47fe78a" />

---

### Step 3 — Add Item to Cart

**POST** `http://localhost:8080/api/cart/1/add`

```json
{
    "productId": 1,
    "quantity": 2
}
```

<img width="1093" height="720" alt="image" src="https://github.com/user-attachments/assets/2a3a8f77-1f60-43e7-9244-bc80792ec7c2" />

---

### Step 4 — View Cart

**GET** `http://localhost:8080/api/cart/1`

<img width="1085" height="855" alt="image" src="https://github.com/user-attachments/assets/9eccc294-c579-4223-9e06-7db6ec5a3afc" />

---

### Step 5 — Place Order

**POST** `http://localhost:8080/api/orders/1/place`

> No request body needed — reads directly from the user's cart.

<img width="1086" height="890" alt="image" src="https://github.com/user-attachments/assets/3fa79dea-007e-4700-b6e8-7e15c8da8c5d" />

---

### Step 6 — View Orders

**GET** `http://localhost:8080/api/orders/user/1`

<img width="1090" height="927" alt="image" src="https://github.com/user-attachments/assets/273e1199-29ee-43cc-bca4-370796570a17" />

---

### Step 7 — Update Order Status

**PATCH** `http://localhost:8080/api/orders/1/status?status=CONFIRMED`

<img width="1084" height="898" alt="image" src="https://github.com/user-attachments/assets/f14c292a-49a0-4934-ad96-c37d4eb4fe98" />

---

### Step 8 — Get All Products 

**GET** `http://localhost:8080/api/products`

<img width="1085" height="804" alt="image" src="https://github.com/user-attachments/assets/51843fb7-e0c7-4384-ae7f-3ac4c52d9735" />

---

## 🚀 Roadmap

### 🔐 Authentication & Security
- [ ] JWT-based authentication (login / logout)
- [ ] Spring Security integration
- [ ] Role-based access control (ADMIN / USER)
- [ ] Password encryption with BCrypt

### 📦 Product Improvements
- [ ] Product categories
- [ ] Product image upload
- [ ] Pagination and sorting for product listing
- [ ] Low stock alerts

### 🛒 Cart & Order Improvements
- [ ] Update item quantity in cart
- [ ] Coupon / discount code system
- [ ] Order cancellation by user
- [ ] Order history with filters

### 💳 Payment
- [ ] Payment integration (Stripe / Midtrans)
- [ ] Payment status tracking
- [ ] Invoice generation

### 🔔 Notifications
- [ ] Email confirmation on registration
- [ ] Order confirmation email
- [ ] Order status update notifications

### 🛠️ Developer Experience
- [ ] Global exception handler
- [ ] Input validation with Bean Validation (`@Valid`)
- [ ] API documentation with Swagger / OpenAPI
- [ ] Unit and integration tests

---

## 👤 Author

**Felix** — [@felixgnwn](https://github.com/felixgnwn)
