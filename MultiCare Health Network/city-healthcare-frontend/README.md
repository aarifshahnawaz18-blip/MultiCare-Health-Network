# CityCare - City-Wide Healthcare Frontend

A Spring Boot + Thymeleaf frontend prototype for a city-wide doctor discovery and appointment platform.

## What is included

### Patient
- Patient registration/login UI
- Patient dashboard
- Search doctors
- Search by department/hospital
- Doctor profile
- Doctor qualification, experience, hospital and fee
- Appointment booking
- Next 15-day calendar
- Time slot selection
- My appointments
- Medical history
- Prescriptions
- Patient profile

### Doctor
- Doctor login UI
- Doctor dashboard
- Upcoming appointments for next 15 days
- Patient search
- Patient details and medical history
- Prescription creation
- Availability / leave management
- Doctor profile

## Important

This is a FRONTEND-ONLY prototype.

- There is no database.
- Login is demo navigation only.
- Data is stored as mock data in JavaScript/localStorage.
- Prescription creation and appointment booking work in browser demo mode.
- External doctor photos use `pravatar.cc`, so internet may be needed for those images.

## Run in Eclipse / STS / IntelliJ

1. Install JDK 17.
2. Import this folder as a Maven project.
3. Wait for Maven dependencies to download.
4. Run:
   `CityHealthcareApplication.java`
5. Open:
   `http://localhost:8080`

Or from terminal:
`mvn spring-boot:run`

## Suggested next backend phase

Later add:
- Spring Data JPA
- MySQL
- Spring Security
- JWT/session authentication
- User/Doctor/Patient entities
- Hospital entity
- Department entity
- Appointment entity
- Prescription entity
- Availability/Leave entity
- REST APIs


## Admin updates and session refresh note
- Admin dashboard now links to `/admin/appointments` and `/admin/doctors`.
- Appointment page lists all appointments ordered by date/time, with text search, date filter, and status filter.
- Doctor Info lists approved doctor registration details and each doctor's upcoming/past appointments, with doctor search.
- For login sessions, open the app consistently at `http://localhost:8080` (do not alternate between `localhost` and `127.0.0.1`). The browser stores the session cookie per host. Refresh/navigation should preserve the session while the Spring Boot process remains running. If the app is restarted, log in again.
