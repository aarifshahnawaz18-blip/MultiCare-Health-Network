# City Healthcare: Spring Boot + MySQL

## Requirements
- JDK 17 or newer
- Maven 3.9+
- MySQL 8+

## Run
1. Open MySQL Workbench and execute `sql/city_healthcare_setup.sql`.
2. In `src/main/resources/application.properties`, set `spring.datasource.password` to your local MySQL password.
3. Import the folder containing `pom.xml` as an Existing Maven Project in Eclipse/IntelliJ.
4. Stop old app instances that use port 8080, then run `CityHealthcareApplication`.
5. Open `http://localhost:8080`.

## Local admin login
Username: `admin`
Password: `ChangeThisAdmin123!`
For local development only. Set environment variables `CITYCARE_ADMIN_USER` and `CITYCARE_ADMIN_PASSWORD` before using outside a local demo.

## Functional flow
- Patient registers; account stays PENDING until admin approval.
- Doctor registers with Aadhaar, PAN, degree, a PDF/image degree certificate (maximum 5 MB), and a profile photo (maximum 3 MB); account stays PENDING.
- Admin reviews the request and can open uploaded certificate from the dashboard, then approve/reject.
- On approval, a row is created in `patients` or `doctors`; shared credentials/status are in `accounts`.
- Only APPROVED users can log in. Login session is retained across refresh and expires after 8 hours of inactivity.
- Patient dashboard/profile/appointments/prescriptions use the logged-in account ID and MySQL.
- Doctor dashboard shows only that doctor's appointments; doctor can issue a prescription for one of their own appointments. Doctor can mark specific dates as days off and submit profile edits for admin approval.
- Prescription has patient_id, doctor_id and appointment_id foreign keys and appears in that patient's prescriptions page.

## Database tables
`accounts`, `doctors`, `patients`, `appointments`, `prescriptions`, `doctor_days_off`, `doctor_profile_changes`.
- Appointment slots run every 15 minutes from 09:00 to 16:45. A database unique constraint on doctor/date/time and backend validation prevent two bookings for the same slot.
- Department pages filter approved doctors by specialization and show their profile photos.
- Appointment rows store both `patient_id` (for relational integrity) and a `patient_name` snapshot for readable SQL queries. Patient account deletion removes that patient's appointments and prescriptions before deleting the account.

## Important security note
This is a local learning/demo implementation, not a production medical-record system. Aadhaar/PAN are sensitive personal data and currently need encryption and tighter operational controls before any real deployment. Uploaded certificates are stored in a local `uploads/degree-certificates` folder and should be backed up and access-controlled.


## Medical history and availability additions
- Doctor dashboard shows only today's appointments; `/doctor-appointments` shows all appointments.
- `/doctor-patients` provides search by patient name, email, or mobile; opening a result shows the patient's appointment history, prescriptions, and uploaded report images.
- `/doctor-availability` lets a doctor mark a future date as a day off. Booking requests for a marked day are rejected by the backend. Existing bookings must be cancelled/rescheduled before marking that date off.
- Patient dashboard includes `My History` at `/patient-history`, with personal details and report-image upload (JPG/PNG recommended, maximum 5 MB). Report metadata is stored in MySQL `patient_reports`; image files are stored in `uploads/patient-reports`.
- The SQL setup script includes the `patient_reports` table. Hibernate `ddl-auto=update` also creates it when the application starts.
