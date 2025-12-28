# 🏦 Banking System - Enterprise Edition

A **comprehensive, production-ready banking system** built with **Spring Boot 3.4.4**, featuring advanced banking operations, loan management, automated recurring payments, and complete audit trails.

## ✨ Highlights

🎯 **40+ RESTful APIs** for complete banking operations  
💰 **Loan Management** with automatic EMI calculations  
🔄 **Recurring Payments** with automated execution  
📊 **Transaction History** with advanced filtering  
👥 **Beneficiary Management** for quick transfers  
🔒 **Enhanced Security** with account freeze/close features  
📝 **Complete Audit Trail** for compliance  
📄 **Account Statements** with comprehensive summaries

## 🚀 Core Features

### Account Management

- ✅ Create multiple account types (Savings, Current, etc.)
- ✅ Check balance and account details
- ✅ Freeze/Unfreeze accounts for security
- ✅ Close accounts (with balance validation)
- ✅ Generate detailed account statements
- ✅ Account status tracking (Active, Frozen, Closed)

### Transaction Operations

- ✅ Deposit and Withdrawal
- ✅ Fund Transfer between accounts
- ✅ Transaction history with pagination
- ✅ Filter by date range, type, amount
- ✅ Recent transactions view
- ✅ Transaction tracking by ID

### Beneficiary Management

- ✅ Add/Update/Delete beneficiaries
- ✅ Quick transfer to saved beneficiaries
- ✅ Beneficiary usage tracking
- ✅ Duplicate prevention

### Loan Management

- ✅ 6 types of loans (Personal, Home, Car, Education, Business, Gold)
- ✅ Complete loan lifecycle (Apply → Approve → Disburse → Repay → Close)
- ✅ Automatic EMI calculation
- ✅ Payment tracking with interest breakdown
- ✅ Loan status management
- ✅ Payment history

### Standing Instructions / Recurring Payments

- ✅ Automated recurring transfers
- ✅ Multiple frequencies (Daily, Weekly, Monthly, Quarterly, Yearly)
- ✅ Pause/Resume/Cancel functionality
- ✅ Automatic execution via scheduler
- ✅ Failure tracking and retry logic

### Audit & Compliance

- ✅ Complete audit trail for all operations
- ✅ User activity tracking
- ✅ Entity change tracking (before/after)
- ✅ IP address logging
- ✅ Date-based audit queries

## 🔐 Security Features

- **JWT Authentication** with token-based access control
- **Role-based Authorization** (ADMIN, MANAGER, CLERK, CASHIER)
- **Account Status Validation** before transactions
- **Transaction Limits** and validation
- **Complete Audit Logging** for compliance

## 🔐 Roles and Permissions

| Role    | Permissions                                                  |
| ------- | ------------------------------------------------------------ |
| ADMIN   | Full access to all modules including user management         |
| MANAGER | Manage customers, accounts, loans, approve/reject operations |
| CLERK   | Create customers, manage accounts, process transactions      |
| CASHIER | View customers, handle deposits/withdrawals                  |

## 🧱 Tech Stack

- **Java**: 17
- **Framework**: Spring Boot 3.4.4
- **Security**: Spring Security + JWT
- **Database**: MySQL 8.0+
- **ORM**: Spring Data JPA + Hibernate
- **Validation**: Jakarta Validation
- **Scheduling**: Spring Scheduling
- **Build Tool**: Maven
- **Documentation**: OpenAPI/Swagger (optional)

## 📊 API Endpoints Summary

| Module                    | Endpoints | Description                                        |
| ------------------------- | --------- | -------------------------------------------------- |
| **Accounts**              | 9         | Create, query, freeze, close, statement generation |
| **Transactions**          | 9         | Deposit, withdraw, transfer, history with filters  |
| **Beneficiaries**         | 5         | CRUD operations for beneficiary management         |
| **Loans**                 | 8         | Complete loan lifecycle management                 |
| **Standing Instructions** | 6         | Recurring payment automation                       |
| **Audit Logs**            | 5         | System audit and compliance tracking               |
| **Customers**             | 3         | Customer management                                |
| **Users**                 | 3         | User management                                    |
| **Authentication**        | 2         | Login and token management                         |

**Total APIs: 50+**

## 🗄️ Database Schema

### Core Tables

- `customer` - Customer information
- `account` - Bank accounts
- `transaction` - All transactions
- `beneficiary` - Saved beneficiaries
- `loan` - Loan applications
- `loan_payment` - Loan EMI payments
- `standing_instruction` - Recurring payment instructions
- `audit_log` - Complete audit trail
- `user`, `role`, `permission` - Authorization
- `address`, `identity` - Customer KYC

## 📁 Project Structure

```
banking_system_backend/
├── src/main/java/com/banking/bankingProject/
│   ├── controllers/          # REST Controllers (10 files)
│   │   ├── AccountController.java
│   │   ├── TransactionController.java
│   │   ├── BeneficiaryController.java
│   │   ├── LoanController.java
│   │   ├── StandingInstructionController.java
│   │   ├── AuditLogController.java
│   │   └── ...
│   │
│   ├── services/             # Business Logic (10 files)
│   │   ├── AccountService.java
│   │   ├── TransactionService.java
│   │   ├── BeneficiaryService.java
│   │   ├── LoanService.java
│   │   └── ...
│   │
│   ├── repositories/         # Data Access (10 files)
│   │   ├── AccountRepository.java
│   │   ├── TransactionRepository.java
│   │   ├── BeneficiaryRepository.java
│   │   └── ...
│   │
│   ├── entities/            # JPA Entities (12 files)
│   │   ├── Account.java
│   │   ├── Transaction.java
│   │   ├── Beneficiary.java
│   │   ├── Loan.java
│   │   ├── LoanPayment.java
│   │   ├── StandingInstruction.java
│   │   └── ...
│   │
│   ├── dto/                 # Data Transfer Objects
│   │   ├── AccountDto.java
│   │   ├── BeneficiaryDto.java
│   │   ├── LoanDto.java
│   │   ├── response/
│   │   │   └── AccountStatementDto.java
│   │   └── ...
│   │
│   ├── enums/               # Enumerations (9 files)
│   │   ├── AccountStatus.java
│   │   ├── TransactionTypeEnum.java
│   │   ├── LoanType.java
│   │   ├── LoanStatus.java
│   │   └── ...
│   │
│   ├── security/            # Security Configuration
│   │   ├── WebSecurityConfig.java
│   │   └── JwtAuthenticationFilter.java
│   │
│   ├── schedulers/          # Scheduled Tasks
│   │   ├── AccountSchedulerService.java
│   │   └── StandingInstructionScheduler.java
│   │
│   ├── exception/           # Exception Handling
│   │   └── BankServiceException.java
│   │
│   └── util/                # Utility Classes
│       ├── ResponseHandler.java
│       └── Utility.java
│
├── src/main/resources/
│   └── application.properties
│
├── pom.xml
└── README.md
```

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- MySQL 8.0+
- Maven 3.6+

### Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/yourusername/banking-system.git
   cd banking-system/banking_system_backend
   ```

2. **Configure MySQL Database**

   ```bash
   mysql -u root -p
   CREATE DATABASE banking_db;
   ```

3. **Update application.properties**

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/banking_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```

4. **Build the project**

   ```bash
   mvn clean install
   ```

5. **Run the application**

   ```bash
   mvn spring-boot:run
   ```

   Or run the JAR:

   ```bash
   java -jar target/bankingProject-0.0.1-SNAPSHOT.jar
   ```

6. **Access the application**
   - Base URL: `http://localhost:8080/api/banking`
   - Swagger UI (if enabled): `http://localhost:8080/swagger-ui.html`

## 📚 API Documentation

### Quick Start Examples

#### 1. Login (Get JWT Token)

```bash
POST /api/banking/authentication/login
{
  "username": "admin",
  "password": "admin123"
}
```

#### 2. Create Customer

```bash
POST /api/banking/customers
Headers: Authorization: Bearer <your-token>
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phone": "1234567890"
}
```

#### 3. Create Account

```bash
POST /api/banking/accounts/create?customerId=CUST_123
Headers: Authorization: Bearer <your-token>
{
  "accountType": "SAVINGS",
  "accountStatus": "ACTIVE",
  "balance": 1000.00
}
```

#### 4. Deposit Money

```bash
POST /api/banking/transactions/deposit?accountNumber=ACC_123&amount=500
Headers: Authorization: Bearer <your-token>
```

#### 5. Transfer Money

```bash
POST /api/banking/transactions/transfer?fromAccount=ACC_123&toAccount=ACC_456&amount=100
Headers: Authorization: Bearer <your-token>
```

#### 6. Get Transaction History

```bash
GET /api/banking/transactions/history/ACC_123
Headers: Authorization: Bearer <your-token>
```

#### 7. Apply for Loan

```bash
POST /api/banking/loans/apply
Headers: Authorization: Bearer <your-token>
{
  "customerId": "CUST_123",
  "accountNumber": "ACC_123",
  "loanType": "PERSONAL_LOAN",
  "loanAmount": 50000,
  "interestRate": 12.5,
  "tenureMonths": 12
}
```

#### 8. Create Standing Instruction

```bash
POST /api/banking/standing-instructions/create
Headers: Authorization: Bearer <your-token>
{
  "fromAccountNumber": "ACC_123",
  "toAccountNumber": "ACC_456",
  "amount": 1000,
  "frequency": "MONTHLY",
  "startDate": "2025-01-01",
  "description": "Rent payment"
}
```

### Full API Documentation

For complete API documentation, see:

- **[API_REFERENCE.md](../API_REFERENCE.md)** - Complete API reference
- **[ENHANCEMENTS.md](../ENHANCEMENTS.md)** - Feature details and examples

## 🏗️ Architecture

See **[ARCHITECTURE.md](../ARCHITECTURE.md)** for detailed architecture diagrams and component interaction.

## 🧪 Testing

### Run Unit Tests

```bash
mvn test
```

### Run Integration Tests

```bash
mvn verify
```

### Test Coverage

```bash
mvn clean test jacoco:report
```

## 📦 Deployment

See **[DEPLOYMENT_CHECKLIST.md](../DEPLOYMENT_CHECKLIST.md)** for complete deployment guide.

### Quick Deployment

```bash
# Build
mvn clean package -DskipTests

# Run
java -jar target/bankingProject-0.0.1-SNAPSHOT.jar --spring.profiles.active=production
```

## 🔒 Security Configuration

### JWT Configuration

- Token expiration: 24 hours (configurable)
- Refresh token: 7 days (configurable)
- Algorithm: HS256

### CORS Configuration

Currently configured for development. Update for production:

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("https://yourdomain.com"));
    // ... other configurations
}
```

## 🔄 Scheduled Tasks

### Account Scheduler

- **Frequency**: Daily at 2:00 AM
- **Purpose**: Mark inactive accounts (no transaction in 90 days)

### Standing Instruction Scheduler

- **Frequency**: Daily at 12:00 AM
- **Purpose**: Execute due recurring payments

## 📊 Database Migration

### Initial Setup

```sql
-- The application will auto-create tables on first run
-- with spring.jpa.hibernate.ddl-auto=update
```

### Manual Schema Creation

```sql
-- See database_schema.sql for complete schema
```

### Indexing for Performance

```sql
CREATE INDEX idx_account_number ON account(account_number);
CREATE INDEX idx_customer_id ON customer(customer_id);
CREATE INDEX idx_txn_account_date ON transaction(account_id, created_date);
CREATE INDEX idx_loan_number ON loan(loan_number);
```

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Error**

   - Verify MySQL is running
   - Check credentials in application.properties
   - Ensure database exists

2. **JWT Token Expired**

   - Login again to get new token
   - Configure longer expiration if needed

3. **Port Already in Use**

   ```properties
   server.port=8081
   ```

4. **Memory Issues**
   ```bash
   java -Xmx2048m -jar bankingProject.jar
   ```

## 📈 Performance Optimization

- Database connection pooling configured
- Query optimization with indexes
- Lazy loading for related entities
- Pagination for large result sets
- Caching for frequently accessed data (Redis recommended)

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Team

- **Developer**: [Your Name]
- **Email**: [your.email@example.com]
- **GitHub**: [yourusername]

## 🙏 Acknowledgments

- Spring Boot Team for excellent framework
- All contributors and testers
- Open source community

## 📞 Support

For support and queries:

- 📧 Email: support@bankingsystem.com
- 📚 Documentation: See `/docs` folder
- 🐛 Issues: GitHub Issues page
- 💬 Discord: [Community Link]

## 📝 Changelog

### Version 2.0.0 (Latest)

- ✨ Added Loan Management System
- ✨ Added Standing Instructions/Recurring Payments
- ✨ Added Beneficiary Management
- ✨ Added Audit Logging
- ✨ Added Account Statement Generation
- ✨ Enhanced Transaction History with filtering
- ✨ Added Account Freeze/Close functionality
- 🐛 Fixed transaction validation issues
- ⚡ Performance improvements

### Version 1.0.0

- ✅ Initial release
- ✅ Basic banking operations
- ✅ User authentication
- ✅ Customer management

---

Made with ❤️ for modern banking solutions

- MySQL (configurable)
- Maven
- Swagger (optional)
- Axios (frontend integration)

## ⚙️ Setup Instructions

1. **Clone the Repository**

```bash
git clone https://github.com/your-username/banking-system.git
cd banking-system
```
