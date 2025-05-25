# Sanzee IAM System Documentation

## Table of Contents
1. [System Overview](#system-overview)
2. [Architecture](#architecture)
3. [Component Diagrams](#component-diagrams)
4. [Class Diagrams](#class-diagrams)
5. [Sequence Diagrams](#sequence-diagrams)
6. [Technical Stack](#technical-stack)
7. [Security Architecture](#security-architecture)
8. [Deployment Architecture](#deployment-architecture)

## System Overview

Sanzee IAM is a comprehensive Identity and Access Management system built using Spring Security and OAuth2. The system provides secure authentication, authorization, and user management capabilities through a microservices architecture.

## Architecture

### High-Level Architecture

```mermaid
graph TB
    subgraph Client Layer
        Client[OAuth2 Client]
        UI[User Interface]
    end

    subgraph Gateway Layer
        Gateway[API Gateway Service]
        RateLimiter[Rate Limiter]
        CircuitBreaker[Circuit Breaker]
    end

    subgraph Authentication Layer
        Auth[Auth Server]
        TokenService[Token Service]
        UserService[User Service]
        DB[(PostgreSQL)]
    end

    subgraph Resource Layer
        Resource[Resource Server]
        PermissionService[Permission Service]
        AuditService[Audit Service]
    end

    %% Client Layer Connections
    UI --> Client
    Client -->|1. Auth Request| Gateway
    
    %% Gateway Layer Connections
    Gateway --> RateLimiter
    RateLimiter --> CircuitBreaker
    CircuitBreaker -->|2. Validate| Auth
    CircuitBreaker -->|3. Forward| Resource
    
    %% Auth Layer Connections
    Auth --> TokenService
    Auth --> UserService
    UserService --> DB
    TokenService --> DB
    
    %% Resource Layer Connections
    Resource --> PermissionService
    Resource --> AuditService
    Resource -->|4. Validate| Auth
    
    %% Styling
    classDef clientLayer fill:#e1f5fe,stroke:#01579b
    classDef gatewayLayer fill:#e8f5e9,stroke:#1b5e20
    classDef authLayer fill:#fff3e0,stroke:#e65100
    classDef resourceLayer fill:#f3e5f5,stroke:#4a148c
    
    class Client,UI clientLayer
    class Gateway,RateLimiter,CircuitBreaker gatewayLayer
    class Auth,TokenService,UserService,DB authLayer
    class Resource,PermissionService,AuditService resourceLayer
```

## Component Diagrams

### Authentication Component

```mermaid
graph TB
    subgraph Auth Server
        AuthController[Auth Controller]
        OAuth2Service[OAuth2 Service]
        TokenService[Token Service]
        UserService[User Service]
        SecurityConfig[Security Config]
    end

    subgraph Database
        UserRepo[User Repository]
        TokenRepo[Token Repository]
        RoleRepo[Role Repository]
    end

    AuthController --> OAuth2Service
    OAuth2Service --> TokenService
    OAuth2Service --> UserService
    UserService --> UserRepo
    UserService --> RoleRepo
    TokenService --> TokenRepo
    SecurityConfig --> OAuth2Service

    classDef controller fill:#e3f2fd,stroke:#1565c0
    classDef service fill:#e8f5e9,stroke:#2e7d32
    classDef repo fill:#fff3e0,stroke:#e65100
    
    class AuthController controller
    class OAuth2Service,TokenService,UserService,SecurityConfig service
    class UserRepo,TokenRepo,RoleRepo repo
```

## Class Diagrams

### Core Domain Model

```mermaid
classDiagram
    class User {
        +Long id
        +String username
        +String email
        +String password
        +Set~Role~ roles
        +boolean enabled
        +createUser()
        +updateUser()
        +deleteUser()
    }

    class Role {
        +Long id
        +String name
        +Set~Permission~ permissions
        +createRole()
        +updateRole()
        +deleteRole()
    }

    class Permission {
        +Long id
        +String name
        +String resource
        +String action
        +createPermission()
        +updatePermission()
        +deletePermission()
    }

    class Token {
        +String accessToken
        +String refreshToken
        +Date expiryDate
        +String username
        +createToken()
        +validateToken()
        +refreshToken()
    }

    User "1" -- "many" Role : has
    Role "1" -- "many" Permission : has
    User "1" -- "many" Token : owns
```

## Sequence Diagrams

### Authentication Flow

```mermaid
sequenceDiagram
    participant Client
    participant Gateway
    participant Auth
    participant Resource
    participant DB

    Client->>Gateway: 1. Login Request
    Gateway->>Auth: 2. Authenticate
    Auth->>DB: 3. Validate Credentials
    DB-->>Auth: 4. User Details
    Auth->>Auth: 5. Generate Tokens
    Auth-->>Gateway: 6. Access & Refresh Tokens
    Gateway-->>Client: 7. Tokens

    Client->>Gateway: 8. Resource Request
    Gateway->>Auth: 9. Validate Token
    Auth-->>Gateway: 10. Token Valid
    Gateway->>Resource: 11. Forward Request
    Resource->>Auth: 12. Check Permissions
    Auth-->>Resource: 13. Permission Granted
    Resource-->>Gateway: 14. Resource Response
    Gateway-->>Client: 15. Final Response
```

### Token Refresh Flow

```mermaid
sequenceDiagram
    participant Client
    participant Gateway
    participant Auth
    participant DB

    Client->>Gateway: 1. Refresh Token Request
    Gateway->>Auth: 2. Validate Refresh Token
    Auth->>DB: 3. Check Token Validity
    DB-->>Auth: 4. Token Status
    Auth->>Auth: 5. Generate New Tokens
    Auth-->>Gateway: 6. New Access Token
    Gateway-->>Client: 7. New Token
```

## Technical Stack

### Backend
- Spring Boot 3.x
- Spring Security
- Spring Cloud Gateway
- Spring Data JPA
- OAuth2
- JWT

### Database
- PostgreSQL
- Redis (for caching)

### Infrastructure
- Docker
- Kubernetes
- AWS/GCP/Azure

## Security Architecture

### Security Layers
1. **Transport Layer Security**
   - HTTPS/TLS
   - Certificate Management
   - Secure Communication

2. **Authentication Layer**
   - OAuth2 Flows
   - JWT Token Management
   - Multi-factor Authentication

3. **Authorization Layer**
   - Role-Based Access Control
   - Resource-Level Permissions
   - Scope-Based Authorization

4. **Data Security**
   - Encryption at Rest
   - Encryption in Transit
   - Secure Password Storage

## Deployment Architecture

```mermaid
graph TB
    subgraph Production Environment
        LB[Load Balancer]
        
        subgraph Gateway Cluster
            G1[Gateway 1]
            G2[Gateway 2]
        end
        
        subgraph Auth Cluster
            A1[Auth Server 1]
            A2[Auth Server 2]
            Cache[(Redis Cache)]
        end
        
        subgraph Resource Cluster
            R1[Resource Server 1]
            R2[Resource Server 2]
        end
        
        subgraph Database Cluster
            DB1[(Primary DB)]
            DB2[(Replica DB)]
        end
    end
    
    LB --> G1
    LB --> G2
    G1 --> A1
    G1 --> A2
    G1 --> R1
    G1 --> R2
    G2 --> A1
    G2 --> A2
    G2 --> R1
    G2 --> R2
    A1 --> Cache
    A2 --> Cache
    A1 --> DB1
    A2 --> DB1
    DB1 --> DB2
    
    classDef prod fill:#f5f5f5,stroke:#333
    classDef cluster fill:#e3f2fd,stroke:#1565c0
    classDef service fill:#e8f5e9,stroke:#2e7d32
    classDef database fill:#fff3e0,stroke:#e65100
    
    class Production Environment prod
    class Gateway Cluster,Auth Cluster,Resource Cluster cluster
    class G1,G2,A1,A2,R1,R2 service
    class DB1,DB2,Cache database
```

## Getting Started

1. Clone the repository
2. Set up PostgreSQL database
3. Configure environment variables
4. Build and run services:
   ```bash
   # Build all services
   mvn clean install
   
   # Run services
   java -jar auth-server/target/auth-server.jar
   java -jar resource-server/target/resource-server.jar
   java -jar gateway-service/target/gateway-service.jar
   java -jar oauth2-client/target/oauth2-client.jar
   ```

## Contributing
Please read CONTRIBUTING.md for details on our code of conduct and the process for submitting pull requests.

## License
This project is licensed under the MIT License - see the LICENSE file for details. 