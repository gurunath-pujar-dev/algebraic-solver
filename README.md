# Algebraic Equation Solver - Spring Boot Application

A RESTful Spring Boot application that stores and evaluates algebraic equations using postfix tree structures.

## Features

- Store algebraic equations in postfix notation with tree structure
- Retrieve all stored equations
- Evaluate equations with variable substitution
- MySQL database integration

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0 or higher

## Database Setup

1. Update `application.properties` with your database credentials:
```yaml
spring.datasource.url=jdbc:mysql://localhost:3306/equation_solver?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

## Installation and Running

1. Clone the repository:
```bash
git clone https://github.com/gurunath-pujar-dev/algebraic-solver.git
cd algebraic-equation-solver
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Running Tests

### Run all tests:
```bash
mvn test
```

### Run specific test classes:
```bash
mvn test -Dtest=EquationServiceTest
mvn test -Dtest=PostfixTreeServiceTest
mvn test -Dtest=EquationControllerTest
```

### Generate test coverage report:
```bash
mvn clean test jacoco:report
```

## Architecture

The application follows a layered architecture:
- **Controller Layer**: REST endpoints
- **Service Layer**: Business logic
- **Repository Layer**: Data access
- **Model Layer**: Data entities and DTOs
