# MicroGate - Production-Grade API Gateway

## Overview

MicroGate is a custom-built API Gateway designed to demonstrate how modern microservice platforms manage traffic, security, service discovery, observability, and fault tolerance.

Unlike business-focused applications, this project focuses on infrastructure engineering concepts commonly used in production environments such as API Gateways, Service Registries, and Traffic Management Layers.

The goal of this project is to understand and implement the core responsibilities of an API Gateway rather than relying entirely on managed gateway solutions.

---

# Features

## Service Registry

- Dynamic service registration
- Service heartbeat mechanism
- Service health monitoring
- Automatic service status updates
- Service instance management

## Routing Engine

- Route-based request forwarding
- Dynamic service lookup
- Path-based routing
- Request forwarding to healthy service instances

## Load Balancing

- Custom Round Robin Load Balancer
- Multiple instances per service
- Automatic traffic distribution
- Instance selection logging

## Authentication & Security

- JWT Authentication
- Token validation
- Protected routes
- Security filters
- Authentication context propagation

## Rate Limiting

- IP-based rate limiting
- Route-level protection
- Abuse prevention
- Request throttling

## Fault Tolerance

- Circuit Breaker implementation
- Failure detection
- Downstream service protection
- Graceful degradation

## Observability

- Request tracing
- Response tracing
- Route logging
- Service instance logging
- Gateway activity monitoring

## Monitoring

- Prometheus Metrics
- Grafana Dashboard Integration
- Gateway performance monitoring

---

# Architecture

```text
Client
   │
   ▼
MicroGate API Gateway
   │
   ├── JWT Authentication
   ├── Rate Limiter
   ├── Router
   ├── Service Registry
   ├── Load Balancer
   ├── Circuit Breaker
   ├── Tracing
   └── Metrics
   │
   ▼
Microservices
```

---

# Project Structure

```text
microgate
│
├── GatewayApplication
│
├── userservice
│
├── mock-service1
│
├── mock-service2
│
├── docs
│
└── README.md
```

---

# Service Registration

Services register themselves with the Gateway Registry.

### Registration Request

```json
{
  "serviceName": "SERVICE1",
  "serviceUrl": "http://localhost:8081",
  "serviceVersion": "1.0",
  "status": "UP"
}
```

### Registry Database

```text
SERVICE1
├── service1-8081
└── service1-8082

SERVICE2
├── service2-8091
└── service2-8092
```

Each instance has:

- Unique Instance ID
- Service URL
- Version Information
- Health Status
- Last Heartbeat Timestamp

---

# Heartbeat Mechanism

Registered services periodically send heartbeat requests.

### Heartbeat Request

```json
{
  "instanceId": "service1-8081"
}
```

The Gateway updates:

- Last heartbeat timestamp
- Service status
- Availability information

Services that stop sending heartbeats are automatically marked as unavailable.

---

# Dynamic Service Discovery

When a request arrives:

```text
/service1/users
```

The gateway:

1. Finds route configuration.
2. Identifies target service.
3. Queries healthy service instances.
4. Selects instance using load balancing strategy.
5. Forwards request.

Example:

```text
SERVICE1

service1-8081
service1-8082
```

Available instances are discovered dynamically from the Service Registry.

---

# Load Balancing

MicroGate includes a custom Round Robin Load Balancer.

Example:

```text
SERVICE1

service1-8081
service1-8082
```

Request Flow:

```text
Request 1 → 8081

Request 2 → 8082

Request 3 → 8081

Request 4 → 8082
```

This distributes traffic evenly across healthy instances.

---

# Running Multiple Service Instances

Only one codebase exists for each service.

Example:

```text
mock-service1
```

can run as:

```text
Instance 1 → Port 8081

Instance 2 → Port 8082
```

using IntelliJ Run Configurations or JVM arguments.

### Example

```bash
java -jar mock-service1.jar --server.port=8081

java -jar mock-service1.jar --server.port=8082
```

Both instances register independently and participate in load balancing.

---

# Authentication Flow

```text
Client
   │
   ▼
JWT Token
   │
   ▼
Gateway Validation
   │
   ▼
Authorized Request
   │
   ▼
Target Service
```

The Gateway validates:

- JWT Signature
- Token Expiration
- Token Type

Invalid requests are rejected before reaching downstream services.

---

# Rate Limiting

The gateway protects services using configurable request limits.

Example:

```text
5 requests per minute
```

If exceeded:

```http
429 Too Many Requests
```

is returned.

This prevents abuse and protects backend services.

---

# Circuit Breaker

MicroGate implements a custom Circuit Breaker mechanism.

Flow:

```text
Healthy Service
      │
      ▼
Failures Detected
      │
      ▼
Circuit Opens
      │
      ▼
Requests Blocked
      │
      ▼
Recovery Check
      │
      ▼
Circuit Closes
```

Benefits:

- Prevents cascading failures
- Protects backend services
- Improves overall system stability

---

# Request Tracing

Every request is traced through the gateway.

Tracked Information:

- Route
- Service Name
- Selected Instance
- Request Duration
- Response Status
- Error Details

Example:

```text
Request Started

Route = /service1/**
Instance = service1-8081

Request Completed

Duration = 25ms
Status = 200 OK
```

---

# Monitoring

Prometheus metrics are exposed for:

- Request Count
- Error Count
- Route Usage
- Response Time
- Gateway Health

Grafana dashboards can be connected for visualization.

---

# Technologies Used

## Backend

- Java 21
- Spring Boot 3
- Spring Security
- Spring WebFlux
- Spring Data JPA
- Hibernate

## Database

- MySQL

## Security

- JWT Authentication

## Monitoring

- Prometheus
- Grafana

## Build Tool

- Maven

---

# Local Development Setup

## Prerequisites

- Java 21
- Maven
- MySQL
- IntelliJ IDEA

---

# Clone Repository

```bash
git clone https://github.com/your-username/microgate.git

cd microgate
```

---

# Environment Configuration

Sensitive configuration files are intentionally excluded from GitHub.

Examples include:

```text
application.yml
application.properties
.env
```

Create your own configuration using:

```yaml
spring:
  datasource:
    url: YOUR_DATABASE_URL
    username: YOUR_USERNAME
    password: YOUR_PASSWORD

jwt:
  secret: YOUR_SECRET_KEY

mail:
  username: YOUR_EMAIL
  password: YOUR_APP_PASSWORD
```

Never commit:

- Database Passwords
- JWT Secrets
- SMTP Credentials
- API Keys
- Access Tokens
- Private Certificates

---

# Running the Gateway

Start MySQL.

Run:

```bash
mvn spring-boot:run
```

or

```bash
java -jar gateway.jar
```

---

# Registering Services

Services register themselves automatically.

Example Registration:

```json
{
  "serviceName": "SERVICE1",
  "serviceUrl": "http://localhost:8081",
  "serviceVersion": "1.0",
  "status": "UP"
}
```

After registration:

```text
Gateway Registry

SERVICE1
├── service1-8081
└── service1-8082
```

---

# Testing Load Balancing

Start:

```text
mock-service1 → 8081
mock-service1 → 8082
```

Send requests:

```http
GET /service1/test
```

Expected:

```text
Response from port 8081

Response from port 8082

Response from port 8081

Response from port 8082
```

This confirms Round Robin Load Balancing is functioning correctly.

---

# Future Improvements

- Least Connections Load Balancer
- Weighted Round Robin
- Distributed Service Registry
- Kubernetes Integration
- Gateway Clustering
- Distributed Cache
- Service Mesh Integration
- Multi-Region Routing
- API Analytics Dashboard

---

# Educational Purpose

This project was built to understand and demonstrate the internal architecture of production API Gateways and Service Registry systems used in modern microservice platforms.

The focus is on infrastructure engineering concepts including:

- Routing
- Authentication
- Service Discovery
- Load Balancing
- Rate Limiting
- Circuit Breaking
- Monitoring
- Observability

rather than business-domain functionality.

---

## Author

**Anmol Kumar**

Backend Engineering • Distributed Systems • Microservices • System Design
