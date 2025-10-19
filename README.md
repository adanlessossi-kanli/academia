# 🎓 Academia - School Administration System

[![Java](https://img.shields.io/badge/Java-24-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

A comprehensive Spring Boot application for managing educational institutions, providing a complete solution for core academic operations including student management, teacher administration, class organization, grading, and attendance tracking.

## ✨ Features

### Core Functionality
- 👥 **Student Management**: Complete registration and academic tracking for grades 1-6
- 👨‍🏫 **Teacher Administration**: Comprehensive teacher profiles with subject specializations
- 🏫 **Class Organization**: Advanced school class management with room assignments
- 📊 **Grading System**: Flexible semester-based academic performance tracking
- 📅 **Attendance Tracking**: Real-time daily attendance monitoring and reporting

### API Features
- 🔄 **Dual API Support**: Both REST and GraphQL endpoints
- 📚 **Interactive Documentation**: Swagger UI for API exploration
- 🔒 **Security**: Built-in authentication and authorization
- ✅ **Data Validation**: Comprehensive input validation and error handling

## 🛠️ Technical Stack

| Component | Technology | Version |
|-----------|------------|----------|
| **Framework** | Spring Boot | 3.5.5 |
| **Language** | Java | 24 |
| **Database** | H2 Database | In-memory |
| **APIs** | REST + GraphQL | - |
| **Security** | Spring Security | - |
| **Documentation** | OpenAPI/Swagger | 2.2.0 |
| **Testing** | JUnit + JaCoCo | - |
| **Build Tool** | Maven | - |
| **Code Enhancement** | Lombok | - |

## Architecture

```
src/main/java/tg/academia/administration/
├── controller/     # REST API endpoints
├── resolver/       # GraphQL handlers
├── entity/         # JPA entities
├── repository/     # Data access layer
├── service/        # Business logic
├── security/       # Authentication config
└── config/         # Application configuration
```

## API Endpoints

### REST API
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- API Docs: `http://localhost:8080/api-docs`

### GraphQL
- Endpoint: `http://localhost:8080/graphql`
- GraphiQL: Available in development mode

## 🚀 Quick Start

### Prerequisites
- Java 24 or higher
- Maven 3.6+ (or use included wrapper)

### Running the Application

```bash
# Clone the repository
git clone <repository-url>
cd academia

# Run the application
./mvnw spring-boot:run

# Or on Windows
mvnw.cmd spring-boot:run
```

### Testing

```bash
# Run all tests
./mvnw test

# Run tests with coverage report
./mvnw test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

### Development Tools

| Tool | URL | Description |
|------|-----|-------------|
| **Application** | http://localhost:8080 | Main application |
| **Swagger UI** | http://localhost:8080/swagger-ui.html | REST API documentation |
| **GraphQL Playground** | http://localhost:8080/graphql | GraphQL interface |
| **H2 Console** | http://localhost:8080/h2-console | Database management |
| **API Docs** | http://localhost:8080/api-docs | OpenAPI specification |

## 📊 Database Schema

### Core Entities

| Entity | Description | Key Fields |
|--------|-------------|------------|
| **Student** | Student information and enrollment | `id`, `firstName`, `lastName`, `grade`, `email` |
| **Teacher** | Teacher profiles and credentials | `id`, `firstName`, `lastName`, `email`, `subject` |
| **SchoolClass** | Class organization and management | `id`, `name`, `grade`, `room`, `teacherId` |
| **Grade** | Academic performance tracking | `id`, `studentId`, `subject`, `score`, `semester` |
| **Attendance** | Daily attendance records | `id`, `studentId`, `date`, `status` |
| **User** | System authentication | `id`, `username`, `password`, `role` |

### Relationships
- Student ↔ SchoolClass (Many-to-One)
- Teacher ↔ SchoolClass (One-to-Many)
- Student ↔ Grade (One-to-Many)
- Student ↔ Attendance (One-to-Many)

## 🔧 Configuration

### Database Connection (H2)
```properties
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (empty)
```

### Environment Variables
```bash
# Optional: Override default port
SERVER_PORT=8080

# Optional: Database configuration
SPRING_DATASOURCE_URL=jdbc:h2:mem:testdb
```

## 📝 API Examples

### REST API
```bash
# Get all students
curl -X GET http://localhost:8080/api/students

# Create a new student
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{"firstName":"John","lastName":"Doe","grade":3,"email":"john.doe@school.edu"}'
```

### GraphQL
```graphql
# Query students by grade
query {
  studentsByGrade(grade: 3) {
    id
    firstName
    lastName
    email
    schoolClass {
      name
      room
    }
  }
}

# Create a new student
mutation {
  createStudent(
    firstName: "Jane"
    lastName: "Smith"
    grade: 4
    email: "jane.smith@school.edu"
  ) {
    id
    firstName
    lastName
  }
}
```

## 🧪 Testing

The project includes comprehensive testing:

- **Unit Tests**: Individual component testing
- **Integration Tests**: End-to-end API testing
- **Security Tests**: Authentication and authorization
- **Repository Tests**: Data layer validation
- **GraphQL Tests**: Query and mutation testing

### Test Coverage
Generate and view test coverage reports:
```bash
./mvnw clean test jacoco:report
open target/site/jacoco/index.html
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Check the [API documentation](http://localhost:8080/swagger-ui.html)
- Review the test cases for usage examples