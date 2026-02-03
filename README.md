# User Management System (Spring Boot Microservice)

<img align="right" alt="User Management Diagram" width="300" src="https://user-images.githubusercontent.com">

This project is a robust **User Management** microservice built using **Spring Boot** and **Java**. It provides essential functionalities for user creation, management, and secure authentication using **Basic Security** and **JWT (JSON Web Tokens)**.

---

## 🚀 Key Features

*   **User Creation API:** A secure REST endpoint for registering new users.
*   **Basic Security:** Implementation of Spring Security for API protection.
*   **JWT Authentication:** Generation and validation of tokens for authorized access.
*   **RESTful API Design:** Clean, standard-compliant endpoints for all operations.
*   **Logging & Debugging:** Configured with robust logging (`app.log`) for easy monitoring and troubleshooting.
*   **Maven Build System:** Standardized project structure and dependency management.

---

## 🛠 Technology Stack

| Technology | Version | Description |
| :--- | :--- | :--- |
| **Java** | 8+ | Core programming language |
| **Spring Boot** | 2.x+ | Application framework |
| **Spring Security** | | Authentication & Authorization |
| **JWT** | | Secure token-based access |
| **Maven** | | Dependency management |
| **PostgreSQL / MySQL** | (Add your DB) | Database persistence |

---

## ⚙️ Getting Started (Setup & Run Locally)

To get a local copy up and running, follow these simple steps.

### Prerequisites

*   **JDK 8+** installed
*   **Maven** installed
*   An IDE (e.g., IntelliJ IDEA, Eclipse)

### Installation & Run

1.  **Clone the repository:**

    ```bash
    git clone https://github.com/utkarsh15153977/User-Management-System
    ```

2.  **Configure Database:**
    Update your database connection details in the `src/main/resources/application.properties` file:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/userdb
    spring.datasource.username=yourusername
    spring.datasource.password=yourpassword
    ```

3.  **Build the project using Maven:**

    ```bash
    ./mvnw clean install
    ```

4.  **Run the application:**

    ```bash
    ./mvnw spring-boot:run
    ```
    The application will start on `http://localhost:9090`.

---

## 🔄 Project Status & Contribution

This project is in active development.

*   **Frequent Feature Additions:** I am continuously working on enhancing the system's functionality, adding new endpoints and services regularly.
*   **Bug Fixes:** The codebase is actively monitored, and bug fixes are prioritized and implemented promptly to maintain system stability and reliability.

---

## 🔒 Security Implementation

The system uses **Spring Security** configured with **JWT**.
*   User credentials are validated upon login, issuing a unique JWT.
*   Subsequent requests require this token in the `Authorization: Bearer <token>` header to access protected resources.

---

## 📄 Logging

Application logs are configured to output to the console by default and also saved to `app.log` in the root directory for persistent monitoring.

---

## 📫 How to Reach Me

If you have any questions or feedback:

*   Email: `utkarshbitmesraranchi@gmail.com`
*   LinkedIn: [Utkarsh Singh](https://www.linkedin.com/in/utkarsh-singh-5a45851b4/)
*   GitHub: [@utkarsh15153977](https://github.com/utkarsh15153977)

***

