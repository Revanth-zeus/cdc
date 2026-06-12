-- Smart Clinic Management System — Seed Data
-- Run AFTER the schema is created (via JPA ddl-auto or manual DDL)

USE smart_clinic;

-- ── Doctors ──────────────────────────────────────────────────────
INSERT INTO doctor (first_name, last_name, email, password, speciality, phone) VALUES
('John',    'Smith',   'john.smith@clinic.com',   '$2a$10$example_bcrypt_hash_1', 'Cardiology',  '+1-555-0101'),
('Emily',   'Brown',   'emily.brown@clinic.com',  '$2a$10$example_bcrypt_hash_2', 'Dermatology', '+1-555-0102'),
('Michael', 'Davis',   'michael.davis@clinic.com','$2a$10$example_bcrypt_hash_3', 'Neurology',   '+1-555-0103'),
('Sarah',   'Wilson',  'sarah.wilson@clinic.com', '$2a$10$example_bcrypt_hash_4', 'Pediatrics',  '+1-555-0104');

-- ── Doctor available times ────────────────────────────────────────
INSERT INTO doctor_available_times (doctor_id, available_time) VALUES
(1,'09:00'),(1,'10:00'),(1,'11:00'),(1,'14:00'),(1,'15:00'),
(2,'09:00'),(2,'10:00'),(2,'11:00'),(2,'14:00'),
(3,'10:00'),(3,'11:00'),(3,'15:00'),(3,'16:00'),
(4,'09:00'),(4,'10:00'),(4,'13:00'),(4,'14:00');

-- ── Patients ──────────────────────────────────────────────────────
INSERT INTO patient (first_name, last_name, email, password, phone, date_of_birth) VALUES
('Alice',   'Johnson', 'alice@example.com',   '$2a$10$example_bcrypt_hash_p1', '+1-555-0201', '1990-03-15'),
('Bob',     'Martinez','bob@example.com',     '$2a$10$example_bcrypt_hash_p2', '+1-555-0202', '1985-07-22'),
('Carol',   'White',   'carol@example.com',   '$2a$10$example_bcrypt_hash_p3', '+1-555-0203', '1992-11-08'),
('David',   'Lee',     'david@example.com',   '$2a$10$example_bcrypt_hash_p4', '+1-555-0204', '1978-05-30'),
('Eva',     'Garcia',  'eva@example.com',     '$2a$10$example_bcrypt_hash_p5', '+1-555-0205', '1995-09-12'),
('Frank',   'Taylor',  'frank@example.com',   '$2a$10$example_bcrypt_hash_p6', '+1-555-0206', '1988-01-25'),
('Grace',   'Anderson','grace@example.com',   '$2a$10$example_bcrypt_hash_p7', '+1-555-0207', '2000-06-18'),
('Henry',   'Thomas',  'henry@example.com',   '$2a$10$example_bcrypt_hash_p8', '+1-555-0208', '1975-12-03');

-- ── Appointments ──────────────────────────────────────────────────
INSERT INTO appointment (doctor_id, patient_id, appointment_time, status, notes) VALUES
(1, 1, '2024-06-15 09:00:00', 'COMPLETED', 'Annual checkup'),
(1, 2, '2024-06-15 10:00:00', 'COMPLETED', 'Chest pain follow-up'),
(1, 3, '2024-06-16 09:00:00', 'SCHEDULED', 'Blood pressure review'),
(2, 4, '2024-06-15 10:00:00', 'COMPLETED', 'Skin rash'),
(2, 5, '2024-06-17 14:00:00', 'SCHEDULED', 'Acne treatment'),
(3, 1, '2024-06-18 10:00:00', 'SCHEDULED', 'Migraine follow-up'),
(3, 6, '2024-06-15 11:00:00', 'COMPLETED', 'Headaches'),
(4, 7, '2024-06-15 09:00:00', 'COMPLETED', 'Routine pediatric exam'),
(4, 8, '2024-06-20 13:00:00', 'SCHEDULED', 'Vaccination'),
(1, 4, '2024-07-01 14:00:00', 'SCHEDULED', 'EKG review'),
(1, 5, '2024-07-02 15:00:00', 'SCHEDULED', 'Stress test'),
(2, 6, '2024-07-03 09:00:00', 'SCHEDULED', 'Follow-up visit');

-- ── Prescriptions ─────────────────────────────────────────────────
INSERT INTO prescription (appointment_id, doctor_id, patient_id, medication, dosage, instructions) VALUES
(1, 1, 1, 'Lisinopril', '10mg', 'Take once daily in the morning'),
(2, 1, 2, 'Metoprolol', '25mg', 'Take twice daily with food'),
(4, 2, 4, 'Hydrocortisone cream', '1%', 'Apply to affected area twice daily'),
(7, 3, 6, 'Sumatriptan', '50mg', 'Take at onset of migraine, max 2/day'),
(8, 4, 7, 'Amoxicillin', '250mg', 'Three times daily for 10 days');
