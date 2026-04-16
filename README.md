<div align="center">

# 🎟️ Event Management System

### A production-ready RESTful API built with Spring Boot 3 & Oracle DB

[![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-brightgreen?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![Oracle DB](https://img.shields.io/badge/Oracle-21c-red?style=flat-square&logo=oracle)](https://www.oracle.com/database/)
[![Liquibase](https://img.shields.io/badge/Liquibase-migrations-blue?style=flat-square)](https://www.liquibase.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)](LICENSE)

**Developed by [Amjad Qattawi](https://github.com/AmjadQattawi)**

</div>

---

## 📌 Overview

The **Event Management System** is a fully-featured backend API that handles the complete lifecycle of events — from creation and publishing to booking, payment processing, and post-event reviews. The system integrates real-time weather data for event locations, sends automated email notifications after successful payments, and uses scheduled jobs to manage booking expirations automatically.

Built as a graduation-to-industry project during a professional Java/Spring Boot internship, it demonstrates real-world backend engineering practices including clean layered architecture, database migrations, async processing, and external API integration.

---

## ✨ Key Features

| Feature | Description |
|---|---|
| 🗂️ **Event Lifecycle Management** | Full CRUD with status workflow: `DRAFT` → `PUBLISHED` → `CANCELLED` |
| 📅 **Booking Engine** | Ticket booking with capacity validation, duplicate prevention, and auto-expiry |
| 💳 **Payment Processing** | Payment creation, validation, and status tracking (`PENDING`, `COMPLETED`, `REFUNDED`, `FAILED`) |
| 🌤️ **Weather Integration** | Auto-fetches live temperature & wind speed for event location via Open-Meteo API |
| 📧 **Email Notifications** | Async payment confirmation emails via Brevo (Sendinblue) API |
| ⭐ **Reviews & Ratings** | Attendees can leave reviews on events they attended |
| 🏆 **Reward Points** | Attendees earn loyalty points per ticket purchased |
| ⏰ **Scheduled Jobs** | Auto-cancels expired bookings; auto-updates event statuses hourly |
| 🔍 **Advanced Search** | Dynamic filtering + pagination on all entities using JPA Specifications |
| 📖 **API Documentation** | Interactive Swagger UI available at `/swagger-ui/index.html` |
| 🔄 **DB Migrations** | Full schema versioning with Liquibase — no manual SQL setup needed |
| 📊 **Health Monitoring** | Spring Boot Actuator endpoints for health, beans, and app info |

---

## 🏗️ Architecture & Design

The project follows a clean **layered architecture** pattern:

```
├── controller       → REST endpoints (request/response handling)
├── service          → Business logic & orchestration
├── interfaceService → Service contracts (interfaces)
├── repository       → JPA data access layer
├── entity           → JPA domain models
├── dto              → Data Transfer Objects (API layer)
├── mapper           → MapStruct entity ↔ DTO mapping
├── specification    → JPA Specifications for dynamic queries
├── searchCriteria   → Search filter objects
├── validator        → Business rule validators
├── exception        → Custom exceptions & global handler
├── enums            → Domain enumerations
├── configuration    → App config (async, scheduling, beans)
└── externalAPIDTO   → DTOs for external API responses
```

### Design Patterns & Principles Applied
- **Repository Pattern** via Spring Data JPA
- **DTO Pattern** with MapStruct for clean API contracts
- **Strategy Pattern** via JPA Specifications for dynamic queries
- **Template Method** via `BaseController` and `BaseService` for shared CRUD
- **Async Processing** for email sending (non-blocking thread pool)
- **Scheduled Tasks** for booking expiry and event status updates

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5 |
| Database | Oracle Database 21c (XE) |
| ORM | Spring Data JPA / Hibernate |
| DB Migrations | Liquibase |
| Object Mapping | MapStruct 1.5.5 |
| Code Generation | Lombok |
| API Docs | SpringDoc OpenAPI (Swagger UI) |
| Email Service | Brevo (Sendinblue) REST API |
| Weather API | Open-Meteo (Geocoding + Forecast) |
| Monitoring | Spring Boot Actuator |
| Build Tool | Maven |

---

## 📂 Data Model

The system manages the following core entities and their relationships:

```
User (abstract)
 ├── Attendee     → can create Bookings, write Reviews, earn RewardPoints
 └── Organizer    → can be assigned to Events
 └── Admin        → manages the platform

Category         → groups Events by type

Event            → has Category, many Organizers, many Bookings, many Reviews
                   → stores live weather data (temperature, windspeed)

Booking          → links Attendee ↔ Event, tracks ticket count & total price
                   → has one Payment, expires after 15 minutes if unpaid

Payment          → linked to one Booking, triggers email notification on success

Review           → written by Attendee on an Event, includes rating & comment
```

---

## 🚀 Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+
- Oracle Database 21c (XE) running locally on port `1521`
- A [Brevo](https://www.brevo.com/) account for email notifications

### 1. Clone the Repository

```bash
git clone https://github.com/AmjadQattawi/event-management-system.git
cd event-management-system
```

### 2. Configure Environment Variables

Create a `.env` file or set these variables in your environment before running:

```bash
BREVO_API_KEY=your_brevo_api_key_here
BREVO_SENDER_EMAIL=your_verified_sender@example.com
BREVO_SENDER_NAME=Event Management System
```

### 3. Configure the Database

Open `src/main/resources/application.properties` and update your Oracle credentials:

```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521/xe
spring.datasource.username=YOUR_SCHEMA_NAME
spring.datasource.password=YOUR_PASSWORD
```

> **Note:** Liquibase will automatically create all tables and sequences on first run. No manual SQL execution required.

### 4. Run the Application

```bash
./mvnw spring-boot:run
```

The server starts on **port 9090** by default.

---

## 📖 API Documentation

Once the application is running, visit:

```
http://localhost:9090/swagger-ui/index.html
```

All endpoints are documented and testable directly from the browser.

### API Endpoints Summary

| Resource | Base URL | Operations |
|---|---|---|
| Events | `/Event` | Create, Read, Update, Delete, Search, Paginate |
| Bookings | `/Booking` | Create, Read, Update, Delete, Search, Paginate |
| Payments | `/Payment` | Create, Read, Update, Delete, Search, Paginate |
| Attendees | `/Attendee` | Create, Read, Update, Delete, Search, Paginate |
| Organizers | `/Organizer` | Create, Read, Update, Delete, Search, Paginate |
| Categories | `/Category` | Create, Read, Update, Delete, Search, Paginate |
| Reviews | `/Review` | Create, Read, Update, Delete, Search, Paginate |
| Admin | `/Admin` | Create, Read, Update, Delete, Search, Paginate |

---

## ⚙️ Configuration Reference

Key configurable properties in `application.properties`:

```properties
# Server
server.port=9090

# Booking expiry (minutes)
booking.expiration.minutes=15

# Scheduler: check for expired bookings every minute
booking.cancel.rate=60000

# Scheduler: update event statuses every hour
event.update.cron=0 0 * * * *

# Actuator endpoints
management.endpoints.web.exposure.include=health,beans,info
```

---

## 📊 Business Logic Highlights

### Booking Flow
1. Attendee requests a booking → system validates event is `PUBLISHED` and has available capacity
2. Booking created with status `PENDING` and capacity is reserved
3. Attendee must complete payment within **15 minutes** or booking is auto-cancelled
4. On successful payment → booking moves to `CONFIRMED` and attendee earns reward points

### Payment Rules
- Payment amount must exactly match the booking's total price
- Cannot pay for a `CANCELLED` booking or event
- Completed payments cannot be deleted (financial record preservation)
- Cancellation triggers automatic `REFUNDED` status on associated payment

### Weather Integration
- On event creation, the system geocodes the event location using Open-Meteo's Geocoding API
- Then fetches current weather conditions (temperature + wind speed)
- Data is stored with the event for attendee awareness

---

## 🔍 Search & Filtering

All major entities support dynamic search with pagination. Example query:

```
GET /Event/search?name=Tech&location=Amman&eventStatus=PUBLISHED&page=0&size=10
```

Filters are combined dynamically using **JPA Specifications** — only the provided fields are applied.

---

## 📧 Async Email Notifications

Email sending runs on a dedicated thread pool to keep the payment response fast:

- **Core threads:** 5
- **Max threads:** 10
- **Queue capacity:** 100
- **Thread prefix:** `Email-Thread-`

---

## 🏥 Health & Monitoring

```
GET http://localhost:9090/actuator/health   → Application health status
GET http://localhost:9090/actuator/beans    → All Spring beans
GET http://localhost:9090/actuator/info     → App info
```

---

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/eventmanagement/event_management_system/
│   │   ├── EventManagementSystemApplication.java
│   │   ├── configuration/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── enums/
│   │   ├── exception/
│   │   ├── externalAPIDTO/
│   │   ├── interfaceService/
│   │   ├── mapper/
│   │   ├── repository/
│   │   ├── searchCriteria/
│   │   ├── service/
│   │   ├── specification/
│   │   └── validator/
│   └── resources/
│       ├── application.properties
│       └── db/changelog/
│           ├── db.changelog-master.xml
│           └── db.changelog-changes.xml
└── test/
```

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome. Feel free to open a pull request or submit an issue.

---

## 👤 Author

**Amjad Qattawi**
- GitHub: [@AmjadQattawi](https://github.com/AmjadQattawi)

---

<div align="center">

⭐ If you found this project useful, please consider giving it a star!

</div>
