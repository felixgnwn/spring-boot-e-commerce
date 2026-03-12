# E-Commerce Backend Project

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
│   ├── OrderController.java
│   └── CouponController.java
├── service/
│   ├── ProductService.java
│   ├── UserService.java
│   ├── CartService.java
│   ├── OrderService.java
│   └── CouponService.java
├── repository/
│   ├── ProductRepository.java
│   ├── UserRepository.java
│   ├── CartRepository.java
│   ├── CartItemRepository.java
│   ├── OrderRepository.java
│   └── CouponRepository.java
├── entity/
│   ├── Product.java
│   ├── User.java
│   ├── Cart.java
│   ├── CartItem.java
│   ├── Order.java
│   ├── OrderItem.java
│   ├── OrderStatus.java
│   └── Coupon.java
└── dto/
    ├── ProductRequest.java
    ├── ProductResponse.java
    ├── UserRequest.java
    ├── UserResponse.java
    ├── CartItemRequest.java
    ├── CartItemResponse.java
    ├── CartResponse.java
    ├── OrderItemResponse.java
    ├── OrderResponse.java
    ├── CouponRequest.java
    └── CouponResponse.java
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
| `PATCH` | `/api/cart/{userId}/update` | Update item quantity in cart |
| `DELETE` | `/api/cart/{userId}/remove/{productId}` | Remove item from cart |
| `DELETE` | `/api/cart/{userId}/clear` | Clear entire cart |

### 📋 Orders

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/orders/{userId}/place` | Place order from cart (optional `couponCode` query param) |
| `GET` | `/api/orders/user/{userId}` | Get all orders for user (with optional filters) |
| `GET` | `/api/orders/{orderId}` | Get order by ID |
| `PATCH` | `/api/orders/{orderId}/status?status=` | Update order status |
| `POST` | `/api/orders/{orderId}/cancel?userId=` | Cancel order by user |

**Order Status values:** `PENDING` `CONFIRMED` `SHIPPED` `DELIVERED` `CANCELLED`

#### Order filters

`GET /api/orders/user/{userId}` supports optional query parameters:

- `status` — filter by order status  
- `startDate` — ISO date-time (e.g. `2026-03-01T00:00:00`)  
- `endDate` — ISO date-time (e.g. `2026-03-31T23:59:59`)

Example:

```text
GET /api/orders/user/1?status=CONFIRMED&startDate=2026-03-01T00:00:00&endDate=2026-03-31T23:59:59
```

---

### 🎟️ Coupons

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/coupons` | Create a coupon |
| `GET` | `/api/coupons` | Get all coupons |
| `PATCH` | `/api/coupons/{id}/deactivate` | Deactivate a coupon |

To apply a coupon when placing an order:

```text
POST /api/orders/{userId}/place?couponCode=SUMMER10
```

---

## 🧪 Testing with Postman

### Happy Path Flow

```text
Register User → Create Product → Add to Cart → View Cart → Place Order → Apply Coupon → Update Status → Cancel Order
```

All requests are made against `http://localhost:8080`.

1. **Register User**  
   `POST /api/users/register`

2. **Create Product**  
   `POST /api/products`

3. **Add Item to Cart**  
   `POST /api/cart/{userId}/add`

4. **Update Item Quantity in Cart**  
   `PATCH /api/cart/{userId}/update`

5. **View Cart**  
   `GET /api/cart/{userId}`

6. **Create Coupon (optional)**  
   `POST /api/coupons`

7. **Place Order (optionally with couponCode)**  
   `POST /api/orders/{userId}/place?couponCode=SUMMER10`

8. **View Orders**  
   `GET /api/orders/user/{userId}`

9. **Filter Orders**  
   `GET /api/orders/user/{userId}?status=CONFIRMED&startDate=2026-03-01T00:00:00&endDate=2026-03-31T23:59:59`

10. **Update Order Status**  
    `PATCH /api/orders/{orderId}/status?status=CONFIRMED`

11. **Cancel Order (by user)**  
    `POST /api/orders/{orderId}/cancel?userId={userId}`

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
- [x] Update item quantity in cart
- [x] Coupon / discount code system
- [x] Order cancellation by user
- [x] Order history with filters

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
