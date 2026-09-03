# Phone-pay-clone-android-fintech
Full-stack Android fintech app simulating real-time money transfers and wallet management using Spring Boot, Spring Security (JWT), and MySQL.
# 📱 PhonePe Clone – Full-Stack Fintech Platform

A robust, full-stack digital wallet and payment application built with a native **Android (Java)** frontend and a **Spring Boot 3.2.x** RESTful backend, backed by **MySQL**. The system implements secure stateless authentication using **JWT (JSON Web Tokens)** and **BCrypt** password encryption.

---

## 🚀 Key Features

* **User Authentication:** Secure registration and login workflows with BCrypt password hashing.
* **Stateless Authorization:** Role-based access and protected endpoints secured via JSON Web Tokens (JWT).
* **Digital Wallet Management:** Auto-creation of digital wallets upon registration, real-time balance tracking, and secure PIN validation.
* **RESTful Networking:** Asynchronous API consumption on Android using Retrofit2 and OkHttp.
* **Session Management:** Secure token caching via `SharedPreferences` with complete back-stack clearance on logout to prevent unauthorized access.
* **Material UI:** Modern, responsive Android UI utilizing `MaterialCardView`, customizable buttons, and input validation.

---

## 🛠️ Tech Stack

### Frontend (Android Client)
* **Language:** Java
* **IDE:** Android Studio
* **Networking:** Retrofit2, OkHttp3, Gson Converter
* **UI Components:** AndroidX, Material Design Components, CardView

### Backend (REST API Server)
* **Framework:** Spring Boot 3.2.5
* **Security:** Spring Security 6, JJWT (io.jsonwebtoken 0.11.5)
* **ORM & Database:** Spring Data JPA (Hibernate), MySQL Driver
* **Build Tool:** Maven
* **Language/SDK:** Java 17

---

## 🏗️ System Architecture
[ Android App (Retrofit2) ]
|
| (HTTP / REST + Bearer JWT)
v
[ Spring Boot Security Filter Chain ]
|
| (JwtRequestFilter Validates Token)
v
[ REST Controllers (AuthController / TransactionController) ]
|
v
[ Service Layer (Business Logic + BCrypt) ]
|
v
[ Data Access Layer (Spring Data JPA Repositories) ]
|
v
[ MySQL Database (Users, Wallets, Transactions) ]

## 📂 Project Structure

```text
├── backend/
│   ├── src/main/java/com/PhonePay/Clone/
│   │   ├── config/             # Spring Security, JWT Filter & Utility
│   │   ├── controller/         # Auth & Transaction REST Controllers
│   │   ├── entity/             # JPA Entities (User, Wallet, Transaction)
│   │   ├── repository/         # Spring Data JPA Repositories
│   │   ├── service/            # Business & Transfer Logic
│   │   └── CloneApplication.java
│   └── pom.xml
│
└── frontend/
    └── app/src/main/
        ├── java/com/phonepay/clone/
        │   ├── network/        # Retrofit Client & ApiService Interface
        │   ├── LoginActivity.java
        │   ├── RegisterActivity.java
        │   └── DashboardActivity.java
        └── res/layout/         # XML Screen Layouts


Method,     Endpoint,                            Description,                                          Protected (JWT)
POST,       /api/auth/register,                  Register a new user & initialize wallet,                  No
POST,       /api/auth/login,                     Authenticate user & return JWT token,                     No
GET,        /api/auth/balance/{userId},          Fetch real-time wallet balance,                           Yes
POST,       /api/transaction/transfer,           Execute peer-to-peer money transfer,                      Yes
GET,        /api/transaction/history/{id},       Fetch user passbook/transaction log,                      Yes
