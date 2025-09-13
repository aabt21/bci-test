# BCI Test - User Authentication API

## Overview
This project is a RESTful API for user management and authentication built with Spring Boot. It provides endpoints for user registration and authentication using JWT tokens.

## Prerequisites
- Java 11
- Gradle 7.4
- Git

## Project Setup

### Clone the Repository
```bash
git clone https://github.com/yourusername/bci-test.git
cd bci-test
```

### Build the Project
```bash
./gradlew clean build
```

## Configuration
The application uses an H2 in-memory database by default, which means no additional database setup is required for development purposes.

### Application Properties
The main configuration is in `src/main/resources/application.yml`:

```yaml
spring:
  application:
    name: bci-test
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: update
  h2:
    console:
      enabled: true
```

## Running the Application

### Using Gradle
```bash
./gradlew bootRun
```

### Using Java
```bash
java -jar build/libs/bci-test-0.0.1-SNAPSHOT.jar
```

The application will start on port 8080 by default.

## Accessing the H2 Console
The H2 database console is enabled and can be accessed at:
```
http://localhost:8080/h2-console
```

Use the following connection details:
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave empty)

## API Documentation

### Endpoints

#### User Registration
```
POST /sing-up
```
Request body:
```json
{
  "name": "User Name",
  "email": "user@example.com",
  "password": "password",
  "phones": [
    {
      "number": 123456,
      "cityCode": 1,
      "countryCode": "+1"
    }
  ]
}
```

Request response:
```json
{
  "id": "UUID",
  "name": "User Name",
  "email": "user@example.com",
  "password": "password",
  "phones": [
    {
      "number": 12345,
      "cityCode": 9,
      "countryCode": "+1"
    }
  ],
  "created": "Sep 13, 2025 02:17:13 PM",
  "lastLogin": "Sep 13, 2025 02:17:13 PM",
  "token": "jwt_token",
  "active": true
}
```

#### User Login
```
GET /login
```
Header:
```
Authorization: Bearer {jwt_token}
```

Request response:
```json
{
  "id": "UUID",
  "name": "User Name",
  "email": "user@example.com",
  "password": "password",
  "phones": [
    {
      "number": 12345,
      "cityCode": 9,
      "countryCode": "+1"
    }
  ],
  "created": "Sep 13, 2025 02:17:13 PM",
  "lastLogin": "Sep 13, 2025 03:17:13 PM (updated)",
  "token": "new_jwt_token",
  "active": true
}
```

## Running Tests
```bash
./gradlew test
```

## Technologies Used
- Spring Boot 2.5.14
- Spring Data JPA
- Spring Security
- H2 Database
- JWT for Authentication
- Lombok
- JUnit for Testing

## Architecture Diagrams

### Component Diagram

```mermaid
%% UML Component Diagram using Mermaid Class Diagram syntax
classDiagram
    class UserRestController {
        <<component>>
        +signUp(User) User
        +login(String) User
    }

    class UserService {
        <<component>>
        +createUser(User) User
    }

    class GetUserLoginUseCase {
        <<component>>
        +execute(String) User
    }

    class User {
        <<model>>
        +UUID id
        +String name
        +String email
        +String password
        +List~Phone~ phones
        +LocalDateTime created
        +LocalDateTime lastLogin
        +String token
        +boolean isActive
    }

    class Phone {
        <<model>>
        +Long number
        +Integer cityCode
        +String countryCode
    }

    class UserValidationService {
        <<service>>
        +isValidEmail(String) boolean
        +isValidPassword(String) boolean
    }

    class UserRepositoryAdapter {
        <<adapter>>
        +findByEmail(String) Optional~User~
        +save(User) User
    }

    class UserRepository {
        <<repository>>
        +findByEmail(String) Optional~UserEntity~
        +save(UserEntity) UserEntity
    }

    class JwtService {
        <<service>>
        +generateToken(String) String
        +extractUsername(String) String
    }

    class UserEntity {
        <<entity>>
        +UUID id
        +String name
        +String email
        +String password
        +List~Phone~ phones
        +LocalDateTime created
        +LocalDateTime lastLogin
        +String token
        +boolean isActive
    }

    class PhoneEntity {
        <<entity>>
        +Long number
        +Integer cityCode
        +String countryCode
    }

    %% Interfaces with stereotypes
    class UserPort {
        <<interface>>
        +createUser(User) User
    }

    UserRestController ..> UserService : uses
    UserRestController ..> GetUserLoginUseCase : uses
    UserService ..|> UserPort : implements
    UserService ..> UserValidationService : uses
    UserService ..> UserRepositoryAdapter : uses
    UserService ..> JwtService : uses
    GetUserLoginUseCase ..> UserRepositoryAdapter : uses
    GetUserLoginUseCase ..> JwtService : uses
    UserRepositoryAdapter ..> UserRepository : uses
    UserRepository ..> UserEntity : maps
    UserEntity *-- PhoneEntity : contains
    UserRepositoryAdapter ..> User : maps
    User *-- Phone : contains
    
    namespace ApplicationLayer {
        class UserService
        class GetUserLoginUseCase
        class UserPort
    }

    namespace DomainLayer {
        class User
        class Phone
        class UserValidationService
    }

    namespace InfrastructureLayer {
        class UserRestController
        class UserRepositoryAdapter
        class UserRepository
        class JwtService
        class UserEntity
        class PhoneEntity
    }
```

### Sequence Diagrams

#### Sign-up Endpoint

```mermaid
sequenceDiagram
    participant Client
    participant UserRestController
    participant UserService
    participant UserValidationService
    participant JwtService
    participant UserRepositoryAdapter
    participant UserRepository

    Client->>UserRestController: POST /sing-up (User data)
    UserRestController->>UserService: createUser(user)
    UserService->>UserValidationService: isValidEmail(email)
    UserValidationService-->>UserService: validation result
    UserService->>UserValidationService: isValidPassword(password)
    UserValidationService-->>UserService: validation result
    UserService->>UserRepositoryAdapter: findByEmail(email)
    UserRepositoryAdapter->>UserRepository: findByEmail(email)
    UserRepository-->>UserRepositoryAdapter: UserEntity or empty
    UserRepositoryAdapter-->>UserService: User or empty

    alt User exists
        UserService-->>UserRestController: throw IllegalArgumentException
        UserRestController-->>Client: 400 Bad Request
    else User doesn't exist
        UserService->>JwtService: generateToken(email)
        JwtService-->>UserService: JWT token
        UserService->>UserService: Set user properties (token, created, lastLogin, active)
        UserService->>UserRepositoryAdapter: save(user)
        UserRepositoryAdapter->>UserRepository: save(userEntity)
        UserRepository-->>UserRepositoryAdapter: saved UserEntity
        UserRepositoryAdapter-->>UserService: saved User
        UserService-->>UserRestController: created User
        UserRestController-->>Client: 200 OK with User data
    end
```

#### Login Endpoint

```mermaid
sequenceDiagram
    participant Client
    participant UserRestController
    participant GetUserLoginUseCase
    participant JwtService
    participant UserRepositoryAdapter
    participant UserRepository

    Client->>UserRestController: GET /login (with JWT token)
    UserRestController->>UserRestController: Extract token from Authorization header
    UserRestController->>GetUserLoginUseCase: execute(token)
    GetUserLoginUseCase->>JwtService: extractUsername(token)
    JwtService-->>GetUserLoginUseCase: email
    GetUserLoginUseCase->>UserRepositoryAdapter: findByEmail(email)
    UserRepositoryAdapter->>UserRepository: findByEmail(email)
    UserRepository-->>UserRepositoryAdapter: UserEntity or empty
    UserRepositoryAdapter-->>GetUserLoginUseCase: User or empty

    alt User not found
        GetUserLoginUseCase-->>UserRestController: throw RuntimeException
        UserRestController-->>Client: 400 Not Found
    else User found
        GetUserLoginUseCase->>GetUserLoginUseCase: Update lastLogin
        GetUserLoginUseCase->>JwtService: generateToken(email)
        JwtService-->>GetUserLoginUseCase: new JWT token
        GetUserLoginUseCase->>GetUserLoginUseCase: Update token
        GetUserLoginUseCase->>UserRepositoryAdapter: save(user)
        UserRepositoryAdapter->>UserRepository: save(userEntity)
        UserRepository-->>UserRepositoryAdapter: saved UserEntity
        UserRepositoryAdapter-->>GetUserLoginUseCase: saved User
        GetUserLoginUseCase-->>UserRestController: updated User
        UserRestController-->>Client: 200 OK with User data
    end
```
