# Smart Clinic Management System — MySQL Schema Design

## Database: `smart_clinic`

---

## Tables

### 1. `doctor`

| Column           | Data Type      | Constraints                        |
|------------------|----------------|------------------------------------|
| id               | BIGINT         | PRIMARY KEY, AUTO_INCREMENT        |
| first_name       | VARCHAR(100)   | NOT NULL                           |
| last_name        | VARCHAR(100)   | NOT NULL                           |
| email            | VARCHAR(150)   | NOT NULL, UNIQUE                   |
| password         | VARCHAR(255)   | NOT NULL                           |
| speciality       | VARCHAR(100)   | NOT NULL                           |
| phone            | VARCHAR(20)    | NOT NULL                           |
| created_at       | TIMESTAMP      | DEFAULT CURRENT_TIMESTAMP          |

### 2. `patient`

| Column           | Data Type      | Constraints                        |
|------------------|----------------|------------------------------------|
| id               | BIGINT         | PRIMARY KEY, AUTO_INCREMENT        |
| first_name       | VARCHAR(100)   | NOT NULL                           |
| last_name        | VARCHAR(100)   | NOT NULL                           |
| email            | VARCHAR(150)   | NOT NULL, UNIQUE                   |
| password         | VARCHAR(255)   | NOT NULL                           |
| phone            | VARCHAR(20)    | NOT NULL                           |
| date_of_birth    | DATE           | NOT NULL                           |
| created_at       | TIMESTAMP      | DEFAULT CURRENT_TIMESTAMP          |

### 3. `appointment`

| Column             | Data Type      | Constraints                              |
|--------------------|----------------|------------------------------------------|
| id                 | BIGINT         | PRIMARY KEY, AUTO_INCREMENT              |
| doctor_id          | BIGINT         | NOT NULL, FOREIGN KEY → doctor(id)       |
| patient_id         | BIGINT         | NOT NULL, FOREIGN KEY → patient(id)      |
| appointment_time   | DATETIME       | NOT NULL                                 |
| status             | VARCHAR(50)    | DEFAULT 'SCHEDULED'                      |
| notes              | TEXT           |                                          |
| created_at         | TIMESTAMP      | DEFAULT CURRENT_TIMESTAMP                |

### 4. `prescription`

| Column           | Data Type      | Constraints                              |
|------------------|----------------|------------------------------------------|
| id               | BIGINT         | PRIMARY KEY, AUTO_INCREMENT              |
| appointment_id   | BIGINT         | NOT NULL, FOREIGN KEY → appointment(id)  |
| doctor_id        | BIGINT         | NOT NULL, FOREIGN KEY → doctor(id)       |
| patient_id       | BIGINT         | NOT NULL, FOREIGN KEY → patient(id)      |
| medication       | TEXT           | NOT NULL                                 |
| dosage           | VARCHAR(255)   | NOT NULL                                 |
| instructions     | TEXT           |                                          |
| issued_at        | TIMESTAMP      | DEFAULT CURRENT_TIMESTAMP                |

### 5. `doctor_available_times`

| Column           | Data Type      | Constraints                              |
|------------------|----------------|------------------------------------------|
| doctor_id        | BIGINT         | NOT NULL, FOREIGN KEY → doctor(id)       |
| available_time   | VARCHAR(50)    | NOT NULL                                 |

---

## Entity-Relationship Summary

```
doctor (1) ──────< appointment (N) >────── (1) patient
doctor (1) ──────< prescription (N)
patient (1) ─────< prescription (N)
appointment (1) ─< prescription (N)
doctor (1) ──────< doctor_available_times (N)
```

---

## SQL DDL

```sql
CREATE DATABASE IF NOT EXISTS smart_clinic;
USE smart_clinic;

CREATE TABLE doctor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    speciality VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE patient (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    date_of_birth DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE appointment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    appointment_time DATETIME NOT NULL,
    status VARCHAR(50) DEFAULT 'SCHEDULED',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (doctor_id) REFERENCES doctor(id),
    FOREIGN KEY (patient_id) REFERENCES patient(id)
);

CREATE TABLE prescription (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    medication TEXT NOT NULL,
    dosage VARCHAR(255) NOT NULL,
    instructions TEXT,
    issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (appointment_id) REFERENCES appointment(id),
    FOREIGN KEY (doctor_id) REFERENCES doctor(id),
    FOREIGN KEY (patient_id) REFERENCES patient(id)
);

CREATE TABLE doctor_available_times (
    doctor_id BIGINT NOT NULL,
    available_time VARCHAR(50) NOT NULL,
    FOREIGN KEY (doctor_id) REFERENCES doctor(id)
);
```

---

## Stored Procedures

```sql
DELIMITER $$

-- Q21: Daily appointment report by doctor
CREATE PROCEDURE GetDailyAppointmentReportByDoctor(
    IN report_date DATE,
    IN doc_id BIGINT
)
BEGIN
    SELECT
        a.id AS appointment_id,
        CONCAT(p.first_name, ' ', p.last_name) AS patient_name,
        p.phone AS patient_phone,
        a.appointment_time,
        a.status,
        a.notes
    FROM appointment a
    JOIN patient p ON a.patient_id = p.id
    WHERE a.doctor_id = doc_id
      AND DATE(a.appointment_time) = report_date
    ORDER BY a.appointment_time;
END$$

-- Q22: Doctor with most patients by month
CREATE PROCEDURE GetDoctorWithMostPatientsByMonth(
    IN month_num INT,
    IN year_num INT
)
BEGIN
    SELECT
        CONCAT(d.first_name, ' ', d.last_name) AS doctor_name,
        d.speciality,
        COUNT(DISTINCT a.patient_id) AS total_patients
    FROM appointment a
    JOIN doctor d ON a.doctor_id = d.id
    WHERE MONTH(a.appointment_time) = month_num
      AND YEAR(a.appointment_time) = year_num
    GROUP BY d.id
    ORDER BY total_patients DESC
    LIMIT 1;
END$$

-- Q23: Doctor with most patients by year
CREATE PROCEDURE GetDoctorWithMostPatientsByYear(
    IN year_num INT
)
BEGIN
    SELECT
        CONCAT(d.first_name, ' ', d.last_name) AS doctor_name,
        d.speciality,
        COUNT(DISTINCT a.patient_id) AS total_patients
    FROM appointment a
    JOIN doctor d ON a.doctor_id = d.id
    WHERE YEAR(a.appointment_time) = year_num
    GROUP BY d.id
    ORDER BY total_patients DESC
    LIMIT 1;
END$$

DELIMITER ;
```
