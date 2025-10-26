# NAGP VeloTrack – Microservices Application

This project is a **Spring Boot microservices-based application** built as part of the NAGP case study1 assignment.  
It demonstrates a modular architecture with independent microservices communicating with each other.

---

## Architecture Overview

Each microservice is developed, tested, and deployed independently.  
They communicate through REST APIs and message queues ->Kafka

**Microservices included:**
1. **User Service**
    – Manages user registration and secure login/logout with JWT authentication.
    - Password reset/change.
2. **Rental Service**
   – Record rental check-in/check-out events. (produces kafka events when rental starts/ends)
   - View complete history of a bike rentals 
3. **Bike Service**
    – Add/register new bicycles with unique ID, model, and condition.
    - View list of all bicycles with current status (Available, Rented, In Maintenance, Out of Service).
    - Update bicycle details:
        a) Mark bicycles as RENTED when user starts a bike rental, rental service will send kafka async notification to bike service to update bike status
        b) Mark bicycles as AVAILABLE when bike rental ends.
        c) Mark bicycles as In_Maintenance when consumes notification from IOT service.
4. **nagp-fleet**
     - It is a svelte based frontend

---

## ⚙️ Tech Stack

| Component | Technology |
|------------|-------------|
| Language | Java 17 |
| Framework | Spring Boot |
| Build Tool | Maven |
| Database | MySQL (or in-memory H2 for dev) |
| Messaging | Kafka |
| Containerization | Docker |
| Service Discovery | Eureka |
| API Gateway | Spring Cloud Gateway |
| Security | JWT |

---

## Getting Started

### Clone the Repository
```bash
git clone https://github.com/myubhanv/nagp_VeloTrack.git 
cd nagp_VeloTrack
