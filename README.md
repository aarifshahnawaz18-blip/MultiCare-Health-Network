# MultiCare Health Network

A web-based Multi-Hospital Management System developed using Spring Boot, Java, MySQL, and Thymeleaf. The platform helps manage multiple hospitals, doctors, patients, appointments, and prescriptions through a centralized system.

## Features

- **Multi-Hospital Management:** Manage multiple hospitals and their departments.
- **User Authentication:** Secure registration and login functionality.
- **Role-Based Access:** Separate access for Admin, Doctor, and Patient.
- **Patient Management:** Register patients and manage their information.
- **Appointment Booking:** Book appointments with doctors based on hospital and department.
- **Doctor Dashboard:** Manage appointments, availability, and leave.
- **Prescription Management:** Create and manage digital prescriptions.
- **Admin Dashboard:** View hospital records and dashboard statistics.
- **Dynamic Doctor Selection:** Fetch doctors dynamically using the Fetch API.

## Technologies Used

- **Backend:** Java 17, Spring Boot
- **Frontend:** HTML, CSS, Thymeleaf, JavaScript
- **Database:** MySQL
- **ORM:** Spring Data JPA, Hibernate
- **Security:** Spring Security, BCrypt
- **Build Tool:** Maven
- **API:** Spring Web, Fetch API

## Project Structure

```text
MultiCare-Health-Network/
├── src/
│   └── main/
│       ├── java/
│       │   └── .../
│       │       ├── controller/
│       │       ├── entity/
│       │       ├── repository/
│       │       ├── service/
│       │       └── config/
│       └── resources/
│           ├── templates/
│           ├── static/
│           └── application.properties
├── pom.xml
└── README.md
```

*Note: The exact package and folder structure may differ in the project.*

## Prerequisites

Install the following before running the application:

- Java JDK 17 or higher
- MySQL 8.0 or higher
- Apache Maven
- VS Code, IntelliJ IDEA, or Eclipse

## Installation and Setup

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/MultiCare-Health-Network.git
```

### 2. Open the Project

```bash
cd MultiCare-Health-Network
```

### 3. Create the Database

Open MySQL Workbench and execute:

```sql
CREATE DATABASE aapka_healthcare;
```

### 4. Configure Database Credentials

Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/aapka_healthcare
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD

spring.jpa.hibernate.ddl-auto=update
```

Replace `YOUR_MYSQL_PASSWORD` with your MySQL password and retain the other required properties from the project configuration.

### 5. Run the Application

Open a terminal in the project root and execute:

```bash
mvn spring-boot:run
```

### 6. Open in Browser

```text
http://localhost:8080
```

## Demo Login Credentials

If the demo account initializer is enabled, use these credentials:

| Role | Email | Password |
|---|---|---|
| Admin | admin@aapkahealthcare.local | ChangeMe123! |
| Doctor | doctor@aapkahealthcare.local | ChangeMe123! |
| Patient | patient@aapkahealthcare.local | ChangeMe123! |

**Security Note:** These are development credentials. Change or disable demo accounts before deployment.

## Future Enhancements

- Advanced hospital and doctor search
- Complete appointment cancellation and rescheduling
- Doctor approval and management workflows
- Downloadable digital prescriptions
- Advanced reports and analytics
- Email and appointment notifications

## Project Objective

The objective of MultiCare Health Network is to provide a centralized platform for managing multiple hospitals, improving appointment coordination, and simplifying interactions between administrators, doctors, and patients.

## Disclaimer

This project is an educational starter application. It requires further development, security review, and testing before production use or handling real patient information.

