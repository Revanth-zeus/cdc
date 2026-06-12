package com.smartclinic.controller;

import com.smartclinic.model.Patient;
import com.smartclinic.repository.AppointmentRepository;
import com.smartclinic.repository.PatientRepository;
import com.smartclinic.security.TokenService;
import com.smartclinic.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<Patient> register(@RequestBody Patient patient) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.register(patient));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        Map<String, Object> response = patientService.login(body.get("email"), body.get("password"));
        if (Boolean.FALSE.equals(response.get("success"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        return ResponseEntity.ok(response);
    }

    // Get all appointments for a specific patient - Q25 support
    @GetMapping("/{patientId}/appointments")
    public ResponseEntity<?> getPatientAppointments(
            @PathVariable Long patientId,
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ") ||
                !tokenService.validateToken(authHeader.substring(7))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized"));
        }
        return ResponseEntity.ok(appointmentRepository.findByPatientId(patientId));
    }
}
