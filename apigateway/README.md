# SentinelStack API Gateway

A secure, production-ready API Gateway built with Spring Boot featuring JWT authentication, Redis-based rate limiting, request logging, analytics, and comprehensive monitoring.

## 🚀 Features

### Core Features
- ✅ **User Authentication & Authorization** - JWT-based secure authentication
- ✅ **User Profile Management** - Complete CRUD operations for user profiles
- ✅ **API Key Management** - Generate and manage API keys for external access
- ✅ **Redis-based Rate Limiting** - Efficient, distributed rate limiting (per minute/hour/day)
- ✅ **Request Logging & Analytics** - Track all API requests with detailed metrics
- ✅ **Admin Dashboard** - System monitoring and user management
- ✅ **Health Monitoring** - Spring Boot Actuator for system health checks
- ✅ **OpenAPI Documentation** - Interactive Swagger UI for API testing

### Technical Stack
- **Framework:** Spring Boot 3.5.5
- **Language:** Java 21
- **Database:** MySQL 8
- **Cache:** Redis
- **Security:** Spring Security + JWT
- **Documentation:** SpringDoc OpenAPI
- **Build Tool:** Maven

## 📋 Prerequisites

- Java 21 or higher
- MySQL 8.0 or higher
- Redis Server
- Maven 3.6 or higher

## 🛠️ Setup Instructions

### 1. Database Setup

```sql
-- Create database
CREATE DATABASE sentinelstack;

-- Create user
CREATE USER 'sentinel_user'@'localhost' IDENTIFIED BY 'Sentinel@123';
GRANT ALL PRIVILEGES ON sentinelstack.* TO 'sentinel_user'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Redis Setup

**Windows (using Memurai):**
```powershell
# Download and install Memurai from https://www.memurai.com/
# Or use WSL2 with Redis
```

**Linux/Mac:**
```bash
# Install Redis
sudo apt-get install redis-server  # Ubuntu/Debian
brew install redis                  # macOS

# Start Redis
redis-server
```

### 3. Application Configuration

The application is pre-configured in `src/main/resources/application.properties`:
- Database: `localhost:3306/sentinelstack`
- Redis: `localhost:6379`
- Server Port: `8080`

### 4. Build and Run

```bash
# Clone the repository
cd D:\SentinelStack\apigateway

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start at: `http://localhost:8080`

## 📚 API Endpoints

### Authentication

#### Register User
```http
POST /api/users/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "SecurePass123",
  "firstName": "John",
  "lastName": "Doe"
}
```

#### Login
```http
POST /api/users/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "SecurePass123"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 86400000
}
```

### User Profile

#### Get Profile
```http
GET /api/users/profile
Authorization: Bearer {token}
```

#### Update Profile
```http
PUT /api/users/profile
Authorization: Bearer {token}
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com"
}
```

### API Key Management

#### Generate API Key
```http
POST /api/keys
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Production Key"
}

Response:
{
  "id": 1,
  "name": "Production Key",
  "key": "sk_live_abc123xyz789...",
  "createdAt": "2025-11-07T08:00:00",
  "status": "ACTIVE"
}
```

#### List API Keys
```http
GET /api/keys
Authorization: Bearer {token}
```

#### Revoke API Key
```http
DELETE /api/keys/{keyId}
Authorization: Bearer {token}
```

### Analytics

#### Get Analytics Summary
```http
GET /api/analytics/summary?startDate=2025-11-01T00:00:00&endDate=2025-11-07T23:59:59
Authorization: Bearer {token}
```

#### Get Request Count
```http
GET /api/analytics/requests/count
Authorization: Bearer {token}
```

#### Get Status Distribution
```http
GET /api/analytics/requests/by-status
Authorization: Bearer {token}
```

#### Get Recent Requests
```http
GET /api/analytics/requests/recent?limit=50
Authorization: Bearer {token}
```

### Rate Limiting

#### Get Rate Limit
```http
GET /api/rate-limits
Authorization: Bearer {token}
```

#### Update Rate Limit
```http
PUT /api/rate-limits
Authorization: Bearer {token}
Content-Type: application/json

{
  "requestsPerMinute": 100,
  "requestsPerHour": 5000,
  "requestsPerDay": 50000
}
```

#### Get Default Rate Limits
```http
GET /api/rate-limits/default
Authorization: Bearer {token}
```

**Default Limits:**
- 60 requests/minute
- 1000 requests/hour
- 10000 requests/day

### Admin (ADMIN role required)

#### Get Dashboard
```http
GET /api/admin/dashboard
Authorization: Bearer {token}
```

#### List All Users
```http
GET /api/admin/users
Authorization: Bearer {token}
```

#### Delete User
```http
DELETE /api/admin/users/{userId}
Authorization: Bearer {token}
```

### Health & Monitoring

#### Application Health
```http
GET /actuator/health
```

#### Metrics
```http
GET /actuator/metrics
```

## 📖 API Documentation

Interactive API documentation is available at:
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs

## 🔒 Security Features

1. **JWT Authentication** - Secure token-based authentication
2. **Password Encryption** - BCrypt password hashing
3. **Rate Limiting** - Redis-based distributed rate limiting
4. **Role-based Access Control** - USER and ADMIN roles
5. **Request Logging** - Complete audit trail of all requests

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        API Gateway                          │
├─────────────────────────────────────────────────────────────┤
│  ┌──────────────┐  ┌──────────────┐  ┌─────────────────┐  │
│  │   JWT Auth   │→ │ Rate Limit   │→ │ Request Logger  │  │
│  │   Filter     │  │   Filter     │  │    Filter       │  │
│  └──────────────┘  └──────────────┘  └─────────────────┘  │
├─────────────────────────────────────────────────────────────┤
│                      Controllers                            │
│  User │ Profile │ API Key │ Analytics │ Admin │ RateLimit  │
├─────────────────────────────────────────────────────────────┤
│                       Services                              │
│  UserService │ APIKeyService │ AnalyticsService │ etc.      │
├─────────────────────────────────────────────────────────────┤
│                     Repositories                            │
│  UserRepo │ APIKeyRepo │ APIRequestRepo │ RateLimitRepo    │
├─────────────────────────────────────────────────────────────┤
│         MySQL Database          │         Redis            │
│  Users │ APIKeys │ Requests     │  Rate Limit Counters     │
└─────────────────────────────────────────────────────────────┘
```

## 🧪 Testing

### Using cURL

```bash
# Register
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@example.com","password":"Test123","firstName":"Test","lastName":"User"}'

# Login
TOKEN=$(curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"Test123"}' | jq -r '.token')

# Get Profile
curl http://localhost:8080/api/users/profile \
  -H "Authorization: Bearer $TOKEN"

# Generate API Key
curl -X POST http://localhost:8080/api/keys \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Key"}'
```

### Using Postman

1. Import the OpenAPI specification from: `http://localhost:8080/v3/api-docs`
2. Create an environment variable `token` for authentication
3. Test all endpoints using the pre-configured requests

## 📊 Monitoring

### Actuator Endpoints

- **Health:** http://localhost:8080/actuator/health
- **Metrics:** http://localhost:8080/actuator/metrics
- **Info:** http://localhost:8080/actuator/info

### Redis Monitoring

```bash
# Connect to Redis
redis-cli

# Monitor rate limit keys
KEYS rate_limit:*

# Check specific user's rate limit
GET rate_limit:1::minute
GET rate_limit:1::hour
GET rate_limit:1::day
```

## 🐛 Troubleshooting

### Database Connection Issues
```bash
# Test MySQL connection
mysql -u sentinel_user -p -h localhost

# Check if database exists
SHOW DATABASES;
```

### Redis Connection Issues
```bash
# Test Redis connection
redis-cli ping

# Should return: PONG
```

### Application Won't Start
```bash
# Check if ports are available
netstat -an | findstr :8080  # Windows
lsof -i :8080                # Linux/Mac

# Check application logs
tail -f logs/spring.log
```

## 📦 Project Structure

```
apigateway/
├── src/main/java/com/sentinelstack/apigateway/
│   ├── config/           # Configuration classes
│   ├── controller/       # REST controllers
│   ├── entity/           # JPA entities
│   ├── filter/           # Security & logging filters
│   ├── repository/       # Data repositories
│   ├── security/         # Security components
│   ├── service/          # Business logic
│   │   └── impl/         # Service implementations
│   └── dto/              # Data Transfer Objects
├── src/main/resources/
│   ├── application.properties
│   ├── static/           # Static resources
│   └── templates/        # Templates (if any)
└── pom.xml               # Maven dependencies
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📝 License

This project is licensed under the MIT License.

## 👥 Authors

- **SentinelStack Team**

## 🔄 Version History

- **v0.0.1-SNAPSHOT** - Initial release with all core features

## 📞 Support

For issues and questions:
- GitHub Issues: [Create an issue]
- Email: support@sentinelstack.com

---

**Built with ❤️ using Spring Boot**
