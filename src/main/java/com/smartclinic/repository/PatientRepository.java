package com.smartclinic.repository;

import com.smartclinic.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Q8 criterion 1: find by email (derived query)
    Optional<Patient> findByEmail(String email);

    // Q8 criterion 2: find by email OR phone
    @Query("SELECT p FROM Patient p WHERE p.email = :email OR p.phone = :phone")
    Optional<Patient> findByEmailOrPhone(@Param("email") String email,
                                         @Param("phone") String phone);
}
