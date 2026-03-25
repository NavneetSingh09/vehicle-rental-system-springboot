# VRMS — Vehicle Rental Management System

A full-stack vehicle rental management system built with **Spring Boot**, **MySQL**, and **vanilla JavaScript**. Features JWT-based authentication, role-based access control, and a complete rental lifecycle from booking to return.

---

## 📸 Preview

| Login | Dashboard | Orders |
|---|---|---|
| BMW-themed login page with register tab | Flip-tile admin dashboard with live stats | Role-aware rental order management |

---

## Features

### Authentication & Security
- JWT-based stateless authentication
- BCrypt password hashing
- Role-based access control — `ADMIN` and `CUSTOMER`
- Secure JWT filter chain with proper `401` responses on invalid tokens
- Secrets managed via environment variables — never hardcoded

### Admin
- Full fleet management — add, update, delete vehicles
- Create and manage customer profiles with discount rates
- View all rental orders across all customers
- Seed default admin user on startup
- Live dashboard with fleet stats — total vehicles, available, active orders

### Customer
- Self-registration — auto-creates linked customer profile
- Browse available vehicles
- Create rental orders — cost auto-calculated with discount applied
- View and complete own orders
- Update own profile

### Rental Logic
- Availability locking — vehicle marked unavailable on rent
- Cost calculation — `days × dailyRate × (1 - discountRate)`
- Order lifecycle — `ACTIVE` → `COMPLETED`
- Vehicle released back to available on order completion
- `@Transactional` on all multi-step DB operations

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.5, Spring Security |
| Database | MySQL 8, Spring Data JPA, Hibernate |
| Authentication | JWT (jjwt 0.11.5), BCrypt |
| Frontend | Vanilla HTML, CSS, JavaScript |
| Build | Maven |

---

##  Project Structure

```
src/
└── main/
    ├── java/com/example/Onboarding/
    │   ├── config/
    │   │   ├── AdminSeeder.java          # Seeds default admin on startup
    │   │   ├── DataSeeder.java           # Seeds sample vehicles on startup
    │   │   └── GlobalExceptionHandler.java # Handles validation errors globally
    │   ├── controller/
    │   │   ├── AdminController.java      # Admin user management
    │   │   ├── AuthController.java       # Register & login
    │   │   ├── CustomerController.java   # Customer CRUD
    │   │   ├── RentalOrderController.java# Order create & complete
    │   │   └── VehicleController.java    # Vehicle CRUD
    │   ├── dto/
    │   │   ├── AuthRequest.java
    │   │   ├── AuthResponse.java
    │   │   ├── CreateOrderRequest.java
    │   │   └── CreateUserRequest.java
    │   ├── Entity/
    │   │   ├── Customer.java
    │   │   ├── CustomerType.java         # INDIVIDUAL, CORPORATE
    │   │   ├── OrderStatus.java          # ACTIVE, COMPLETED
    │   │   ├── RentalOrder.java
    │   │   ├── Role.java                 # ADMIN, CUSTOMER
    │   │   ├── User.java
    │   │   ├── Vehicle.java
    │   │   └── VehicleType.java          # CAR, SUV, TRUCK
    │   ├── repo/
    │   │   ├── CustomerRepository.java
    │   │   ├── RentalOrderRepository.java
    │   │   ├── UserRepository.java
    │   │   └── VehicleRepository.java
    │   └── security/
    │       ├── JwtAuthFilter.java        # JWT validation filter
    │       ├── JwtService.java           # Token generation & parsing
    │       └── SecurityConfig.java       # Route protection rules
    └── resources/
        ├── application.properties        # Placeholder config (safe to commit)
        └── application-local.properties  # Real secrets (gitignored)

frontend/
├── index.html        # Login & register
├── dashboard.html    # Admin dashboard with flip tiles
├── vehicles.html     # Vehicle management
├── customers.html    # Customer management
└── orders.html       # Rental order management
```

---

##  Setup & Installation

### Prerequisites
- Java 17+
- Maven
- MySQL 8+

### Step 1 — Clone the repository
```bash
git clone https://github.com/your-username/vrms.git
cd vrms
```

### Step 2 — Create the database
```sql
CREATE DATABASE vrms;
```

### Step 3 — Configure `application-local.properties`

Create `src/main/resources/application-local.properties`:
```properties
DB_URL=jdbc:mysql://localhost:3306/vrms?createDatabaseIfNotExist=true
DB_USER=your_mysql_username
DB_PASS=your_mysql_password
JWT_SECRET=your_jwt_secret_min_32_chars
admin.email=admin@vrms.com
admin.password=YourAdminPassword
```

Generate a JWT secret:
```bash
openssl rand -hex 32
```

### Step 4 — Run the application
```bash
mvn spring-boot:run "-Dspring-boot.run.profiles=local"
```

### Step 5 — Open the frontend

Open `frontend/index.html` in your browser and log in with your configured admin credentials.

---

##  API Endpoints

### Auth — Public
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register new customer |
| POST | `/api/auth/login` | Login, returns JWT token |

### Vehicles
| Method | Endpoint | Role | Description |
|---|---|---|---|
| GET | `/api/vehicles/public` | ADMIN, CUSTOMER | Get available vehicles |
| GET | `/api/vehicles` | ADMIN | Get all vehicles |
| POST | `/api/vehicles` | ADMIN | Add vehicle |
| PUT | `/api/vehicles/{id}` | ADMIN | Update vehicle |
| DELETE | `/api/vehicles/{id}` | ADMIN | Delete vehicle |

### Customers
| Method | Endpoint | Role | Description |
|---|---|---|---|
| GET | `/api/customers/me` | ADMIN, CUSTOMER | Get own profile |
| GET | `/api/customers` | ADMIN | Get all customers |
| POST | `/api/customers` | ADMIN | Create customer |
| PUT | `/api/customers/{id}` | ADMIN | Update customer |
| DELETE | `/api/customers/{id}` | ADMIN | Delete customer |

### Orders
| Method | Endpoint | Role | Description |
|---|---|---|---|
| GET | `/api/orders` | ADMIN, CUSTOMER | Get orders (filtered by role) |
| POST | `/api/orders` | ADMIN, CUSTOMER | Create rental order |
| POST | `/api/orders/{id}/complete` | ADMIN, CUSTOMER | Complete & return vehicle |

### Admin
| Method | Endpoint | Role | Description |
|---|---|---|---|
| POST | `/api/admin/users` | ADMIN | Create any user with role |

---

##  Rental Cost Formula

```
totalCost = days × dailyRate × (1 - discountRate)
```

- `days` = endDate - startDate + 1
- `dailyRate` = set per vehicle
- `discountRate` = set per customer (e.g. `0.10` = 10% off)

---

##  Seeded Data

On first startup the app automatically seeds:

**Admin user:**
```
Email:    (configured in application-local.properties)
Password: (configured in application-local.properties)
Role:     ADMIN
```

**Sample vehicles:**
| Make | Model | Type | Rate |
|---|---|---|---|
| Toyota | Camry | CAR | $55/day |
| Honda | Civic | CAR | $45/day |
| Ford | Explorer | SUV | $70/day |
| Chevrolet | Silverado | TRUCK | $85/day |

---

##  Security Notes

- All secrets are stored in `application-local.properties` which is **gitignored**
- JWT tokens expire after **6 hours**
- Passwords are hashed with **BCrypt**
- Invalid/expired tokens return `401 Unauthorized` immediately
- All multi-step database operations are wrapped in `@Transactional`

---

##  Deployment

### Railway (Recommended)
1. Deploy MySQL plugin on Railway
2. Deploy Spring Boot app — set environment variables in Railway dashboard
3. Deploy frontend HTML files on Vercel
4. Update `const API` in all HTML files to your Railway backend URL

### AWS
- Backend → Elastic Beanstalk (Java)
- Database → RDS MySQL
- Frontend → S3 + CloudFront

---

##  Known Limitations / Future Improvements

- [ ] `User` ↔ `Customer` should have a proper `@OneToOne` JPA relationship
- [ ] Unit and integration tests
- [ ] Invoice / receipt generation after order completion
- [ ] Email notifications on booking and return
- [ ] Search and filter vehicles by type, rate, availability
- [ ] Customer profile self-update endpoint

---

##  Author

**Navneet Singh**
DePaul University


---
