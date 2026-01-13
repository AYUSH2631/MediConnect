# MediConnect – Smart Healthcare Microservices Platform

MediConnect is a **production‑grade smart healthcare backend system** built using **Spring Boot microservices architecture**. It digitizes and streamlines core hospital workflows such as authentication, patient & doctor management, appointment scheduling, and notifications, while following **industry‑level backend best practices**.

---

## 🚀 Key Features

* **Microservices Architecture** with clear service separation
* **JWT‑based Authentication & Authorization**
* **API Gateway** for centralized routing & security
* **Kafka‑based Event‑Driven Notifications**
* **Scalable & Fault‑Tolerant Design**
* **Clean REST APIs with Validation & Exception Handling**

---

## 🧩 Services Overview

| Service                  | Description                                      |
| ------------------------ | ------------------------------------------------ |
| **Auth Service**         | User authentication, JWT generation & validation |
| **Patient Service**      | Patient registration, profile management         |
| **Doctor Service**       | Doctor onboarding, specialization & availability |
| **Appointment Service**  | Appointment booking, status management           |
| **Notification Service** | Asynchronous notifications using Kafka           |
| **Admin Service**        | Admin‑level operations & monitoring              |
| **API Gateway**          | Request routing, security enforcement            |
| **UI Component**         | Basic frontend integration (optional layer)      |

---

## 🛠 Tech Stack

* **Backend:** Java, Spring Boot
* **Security:** Spring Security, JWT
* **Architecture:** Microservices, REST APIs
* **Messaging:** Apache Kafka
* **Database:** MongoDB
* **Build Tool:** Maven
* **API Gateway:** Spring Cloud Gateway

---

## 🔐 Security Flow

1. User authenticates via **Auth Service**
2. JWT token is issued
3. Requests pass through **API Gateway**
4. Token is validated before routing to services

---

## 🔄 Event‑Driven Flow (Kafka)

* Appointment events are published to Kafka topics
* Notification Service consumes events
* Enables **loose coupling & async processing**
