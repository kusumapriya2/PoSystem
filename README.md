# PoSystem (Purchase Order Management + Delivery System)

A Spring Boot backend project for managing Purchase Orders (PO), payments, courier assignment, and delivery tracking.  
Includes business validations, stock handling, SMTP email notifications, and delivery workflow.

---

## Features

### ✅ Core Modules
- Admin management (single admin restriction)
- Customer, Vendor, Product management
- Purchase Order creation with items (PoItem)
- Order approval flow with validations
- Stock validation + stock reduction on approval

### ✅ Payment
- Payment records linked to orders
- Supports COD vs non-COD rules
- Dummy payment support (PENDING → SUCCESS/FAILED)
- **Stripe Integration (Planned)**

### ✅ Delivery & Courier
- Courier entity with SLA + priority
- Courier service areas (pincode mapping)
- Auto courier assignment on order approval
- DeliveryDetails lifecycle (PENDING / SHIPPED / IN_TRANSIT / DELIVERED)

### ✅ Notifications
- SMTP email integration for:
  - Order placed
  - Order approved
  - Delivery assigned
  - In transit / shipped / delivered
  - Order rejected

---

## Tech Stack

- Java 17 (or your configured Java version)
- Spring Boot
- Spring Data JPA (Hibernate)
- PostgreSQL
- MapStruct (DTO mapping)
- Lombok
- SMTP (Spring Boot Mail)

---

## Project Structure (High Level)

