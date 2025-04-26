# 🏦 Banking System - Spring Boot Application

A full-featured banking system developed with **Spring Boot**, supporting user role-based access control, customer and account management, and transaction operations like deposits, withdrawals, and transfers.

## 🚀 Features

- User authentication with JWT
- Role-based access control (ADMIN, CLERK, MANAGER, CASHIER)
- Customer creation and search
- Account creation, balance inquiry, and listing
- Transaction operations: deposit, withdraw, transfer
- Global error handling
- RESTful API with proper status codes
- CORS-enabled for frontend integration

## 🔐 Roles and Permissions

| Role    | Permissions |
|---------|-------------|
| ADMIN   | Full access to users, customers, accounts, transactions |
| MANAGER | Manage customers and accounts |
| CLERK   | Create/search customers, manage accounts |
| CASHIER | View/search customers, handle transactions |

## 🧱 Tech Stack

- Java 17+
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- MySQL (configurable)
- Maven
- Swagger (optional)
- Axios (frontend integration)

## ⚙️ Setup Instructions

1. **Clone the Repository**

```bash
git clone https://github.com/your-username/banking-system.git
cd banking-system
