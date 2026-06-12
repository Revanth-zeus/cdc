# Smart Clinic Management System

A full-stack clinic management platform built with Java Spring Boot, MySQL, and HTML/CSS/JS frontend.

## Tech Stack

| Layer     | Technology                        |
|-----------|-----------------------------------|
| Backend   | Java 17, Spring Boot 3.2, JPA     |
| Database  | MySQL 8.0                         |
| Security  | Spring Security + JWT (JJWT)      |
| Frontend  | HTML5, CSS3, Vanilla JavaScript   |
| Container | Docker (multi-stage build)        |
| CI/CD     | GitHub Actions                    |

## Project Structure

```
cdc/
├── .github/workflows/ci.yml          # Q12 - GitHub Actions CI
├── docs/
│   ├── schema-design.md               # Q2  - MySQL schema + stored procedures
│   └── seed.sql                       # Sample data for SQL queries
├── src/main/java/com/smartclinic/
│   ├── SmartClinicApplication.java
│   ├── config/SecurityConfig.java
│   ├── controller/
│   │   ├── DoctorController.java      # Q5
│   │   ├── AppointmentController.java
│   │   ├── PrescriptionController.java # Q7
│   │   └── PatientController.java
│   ├── model/
│   │   ├── Doctor.java                # Q3
│   │   ├── Patient.java
│   │   ├── Appointment.java           # Q4
│   │   └── Prescription.java
│   ├── repository/
│   │   ├── DoctorRepository.java
│   │   ├── PatientRepository.java     # Q8
│   │   ├── AppointmentRepository.java
│   │   └── PrescriptionRepository.java
│   ├── security/TokenService.java     # Q9
│   └── service/
│       ├── DoctorService.java         # Q10
│       ├── AppointmentService.java    # Q6
│       └── PatientService.java
├── frontend/
│   ├── admin/index.html               # Q13, Q16
│   ├── doctor/index.html              # Q14, Q18
│   └── patient/index.html            # Q15, Q17
├── Dockerfile                         # Q11
├── docker-compose.yml
└── pom.xml
```

## Running the Application

### Option 1 — Docker Compose (recommended)

```bash
docker-compose up --build
```

App runs at `http://localhost:8080`

### Option 2 — Local

1. Start MySQL and create database:
   ```sql
   CREATE DATABASE smart_clinic;
   ```
2. Run seed data: `mysql -u root -p smart_clinic < docs/seed.sql`
3. Start backend: `mvn spring-boot:run`
4. Open frontend HTML files in browser

## Frontend Portals

| Portal  | Path                         | Default Credentials        |
|---------|------------------------------|----------------------------|
| Admin   | `frontend/admin/index.html`  | admin@clinic.com / admin123 |
| Doctor  | `frontend/doctor/index.html` | john.smith@clinic.com / doctor123 |
| Patient | `frontend/patient/index.html`| alice@example.com / patient123 |

## REST API Reference

### Doctors
```bash
# Q24: Get all doctors
curl -X GET http://localhost:8080/api/doctors

# Q26: Get doctors by speciality
curl "http://localhost:8080/api/doctors/search?speciality=Cardiology"

# Doctor availability
curl -X GET "http://localhost:8080/api/doctors/1/availability?date=2024-06-15" \
  -H "Authorization: Bearer <token>"
```

### Auth + Appointments
```bash
# Login as patient
curl -X POST http://localhost:8080/api/patients/login \
  -H "Content-Type: application/json" \
  -d '{"email":"alice@example.com","password":"patient123"}'

# Q25: Get patient appointments (replace TOKEN)
curl -X GET http://localhost:8080/api/patients/1/appointments \
  -H "Authorization: Bearer TOKEN"
```

## SQL Queries for Submission

```sql
-- Q19
SHOW TABLES;

-- Q20
SELECT * FROM patient LIMIT 5;

-- Q21
CALL GetDailyAppointmentReportByDoctor('2024-06-15', 1);

-- Q22
CALL GetDoctorWithMostPatientsByMonth(6, 2024);

-- Q23
CALL GetDoctorWithMostPatientsByYear(2024);
```
