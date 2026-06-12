package com.smartclinic.controller;

import com.smartclinic.model.Prescription;
import com.smartclinic.repository.AppointmentRepository;
import com.smartclinic.repository.DoctorRepository;
import com.smartclinic.repository.PatientRepository;
import com.smartclinic.repository.PrescriptionRepository;
import com.smartclinic.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/prescriptions")
@CrossOrigin(origins = "*")
public class PrescriptionController {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private TokenService tokenService;

    // Q7 criterion 1: POST saves a prescription with token and request body validation
    @PostMapping
    public ResponseEntity<?> savePrescription(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody Prescription prescription) {

        // Q7 criterion 2: return structured success or error using ResponseEntity
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Missing authorization token"));
        }

        String token = authHeader.substring(7);
        if (!tokenService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Invalid or expired token"));
        }

        try {
            Prescription saved = prescriptionRepository.save(prescription);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "success", true,
                            "message", "Prescription saved successfully",
                            "prescriptionId", saved.getId()
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Failed to save prescription: " + e.getMessage()));
        }
    }

    // GET prescriptions for a patient
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getPatientPrescriptions(
            @PathVariable Long patientId,
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ") ||
                !tokenService.validateToken(authHeader.substring(7))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Unauthorized"));
        }

        return ResponseEntity.ok(prescriptionRepository.findByPatientId(patientId));
    }
}
