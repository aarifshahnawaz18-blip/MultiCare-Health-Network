CREATE DATABASE IF NOT EXISTS city_healthcare CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE city_healthcare;

-- Shared login and registration requests. New accounts start as PENDING.
CREATE TABLE IF NOT EXISTS accounts (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  full_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(255) NOT NULL,
  status VARCHAR(255) NOT NULL,
  mobile VARCHAR(255),
  age INT,
  specialization VARCHAR(255),
  consultation_fee INT,
  aadhaar_number VARCHAR(20),
  pan_number VARCHAR(10),
  degree VARCHAR(150),
  degree_certificate_path VARCHAR(500),
  profile_photo_path VARCHAR(500),
  created_at DATETIME(6),
  rejection_reason VARCHAR(500)
) ENGINE=InnoDB;

-- These profile rows are created by the application when Admin approves the account.
CREATE TABLE IF NOT EXISTS doctors (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  account_id BIGINT NOT NULL UNIQUE,
  aadhaar_number VARCHAR(255),
  pan_number VARCHAR(255),
  degree VARCHAR(255),
  degree_certificate_path VARCHAR(500),
  specialization VARCHAR(255),
  mobile VARCHAR(255),
  CONSTRAINT fk_doctors_account FOREIGN KEY (account_id) REFERENCES accounts(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS patients (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  account_id BIGINT NOT NULL UNIQUE,
  age INT,
  mobile VARCHAR(255),
  CONSTRAINT fk_patients_account FOREIGN KEY (account_id) REFERENCES accounts(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS appointments (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  patient_id BIGINT NOT NULL,
  patient_name VARCHAR(255) NOT NULL,
  doctor_id BIGINT NOT NULL,
  appointment_date DATE NOT NULL,
  appointment_time VARCHAR(30) NOT NULL,
  status VARCHAR(30) NOT NULL DEFAULT 'BOOKED',
  reason VARCHAR(1000),
  created_at DATETIME(6),
  INDEX idx_appointments_patient (patient_id),
  INDEX idx_appointments_doctor (doctor_id),
  UNIQUE KEY uk_doctor_slot (doctor_id, appointment_date, appointment_time),
  CONSTRAINT fk_appointments_patient FOREIGN KEY (patient_id) REFERENCES accounts(id),
  CONSTRAINT fk_appointments_doctor FOREIGN KEY (doctor_id) REFERENCES accounts(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS prescriptions (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  patient_id BIGINT NOT NULL,
  doctor_id BIGINT NOT NULL,
  appointment_id BIGINT,
  diagnosis VARCHAR(500) NOT NULL,
  medicines VARCHAR(5000) NOT NULL,
  instructions VARCHAR(3000),
  created_at DATETIME(6),
  INDEX idx_prescriptions_patient (patient_id),
  INDEX idx_prescriptions_doctor (doctor_id),
  CONSTRAINT fk_prescriptions_patient FOREIGN KEY (patient_id) REFERENCES accounts(id),
  CONSTRAINT fk_prescriptions_doctor FOREIGN KEY (doctor_id) REFERENCES accounts(id),
  CONSTRAINT fk_prescriptions_appointment FOREIGN KEY (appointment_id) REFERENCES appointments(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS doctor_days_off (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  doctor_id BIGINT NOT NULL,
  off_date DATE NOT NULL,
  UNIQUE KEY uk_doctor_day_off (doctor_id, off_date),
  CONSTRAINT fk_day_off_doctor FOREIGN KEY (doctor_id) REFERENCES accounts(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS doctor_profile_changes (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  doctor_id BIGINT NOT NULL,
  full_name VARCHAR(255) NOT NULL,
  mobile VARCHAR(255) NOT NULL,
  specialization VARCHAR(255) NOT NULL,
  degree VARCHAR(150) NOT NULL,
  photo_path VARCHAR(500),
  consultation_fee INT,
  submitted_at DATETIME(6),
  CONSTRAINT fk_profile_change_doctor FOREIGN KEY (doctor_id) REFERENCES accounts(id)
) ENGINE=InnoDB;

-- For an existing database, Hibernate update adds new columns/tables. Backfill patient names if upgrading old appointment data:
-- UPDATE appointments a JOIN accounts p ON p.id=a.patient_id SET a.patient_name=p.full_name WHERE a.patient_name IS NULL;

-- Upgrade an existing database safely (MySQL 8.0.29+ supports ADD COLUMN IF NOT EXISTS).
ALTER TABLE accounts ADD COLUMN IF NOT EXISTS profile_photo_path VARCHAR(500);
ALTER TABLE accounts ADD COLUMN IF NOT EXISTS consultation_fee INT;
ALTER TABLE doctor_profile_changes ADD COLUMN IF NOT EXISTS consultation_fee INT;
ALTER TABLE appointments ADD COLUMN IF NOT EXISTS patient_name VARCHAR(255) NULL;
UPDATE appointments a JOIN accounts p ON p.id=a.patient_id SET a.patient_name=p.full_name WHERE a.patient_name IS NULL;
ALTER TABLE appointments MODIFY COLUMN patient_name VARCHAR(255) NOT NULL;

-- Check table creation
SHOW TABLES;


-- Patient-uploaded medical report image metadata (actual images are stored in uploads/patient-reports)
CREATE TABLE IF NOT EXISTS patient_reports (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  patient_id BIGINT NOT NULL,
  title VARCHAR(180) NOT NULL,
  notes VARCHAR(1000),
  image_path VARCHAR(255) NOT NULL,
  content_type VARCHAR(100) NOT NULL,
  uploaded_at DATETIME(6),
  CONSTRAINT fk_patient_reports_account FOREIGN KEY (patient_id) REFERENCES accounts(id)
);

-- Public home-page contact messages
CREATE TABLE IF NOT EXISTS contact_messages (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(120) NOT NULL,
  number VARCHAR(30) NOT NULL,
  message VARCHAR(2000) NOT NULL,
  created_at DATETIME(6)
) ENGINE=InnoDB;

-- Patient feedback tied to both patient and the exact doctor account ID
CREATE TABLE IF NOT EXISTS doctor_reviews (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  patient_id BIGINT NOT NULL,
  doctor_id BIGINT NOT NULL,
  rating INT NOT NULL,
  review VARCHAR(1500) NOT NULL,
  created_at DATETIME(6),
  UNIQUE KEY uk_patient_doctor_review (patient_id, doctor_id),
  INDEX idx_reviews_doctor (doctor_id),
  CONSTRAINT fk_reviews_patient FOREIGN KEY (patient_id) REFERENCES accounts(id),
  CONSTRAINT fk_reviews_doctor FOREIGN KEY (doctor_id) REFERENCES accounts(id)
) ENGINE=InnoDB;
