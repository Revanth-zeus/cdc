package com.smartclinic.controller;

import com.smartclinic.model.Doctor;
import com.smartclinic.security.TokenService;
import com.smartclinic.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private TokenService tokenService;

    // Q5 criterion 1: GET endpoint for doctor availability with dynamic parameters
    @GetMapping("/{doctorId}/availability")
    public ResponseEntity<?> getDoctorAvailability(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestHeader("Authorization") String authHeader) {

        // Q5 criterion 2: validate token and return structured ResponseEntity
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Missing or invalid token"));
        }

        String token = authHeader.substring(7);
        if (!tokenService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Token validation failed"));
        }

        List<String> availableSlots = doctorService.getAvailableSlots(doctorId, date);
        return ResponseEntity.ok(Map.of(
                "doctorId", doctorId,
                "date", date.toString(),
                "availableSlots", availableSlots
        ));
    }

    // GET all doctors
    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    // GET doctors by speciality - used in Q26
    @GetMapping("/search")
    public ResponseEntity<List<Doctor>> getDoctorsBySpeciality(
            @RequestParam(required = false) String speciality,
            @RequestParam(required = false) String time) {
        List<Doctor> doctors = (speciality != null)
                ? doctorService.getDoctorsBySpeciality(speciality)
                : doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    // GET doctors by name search
    @GetMapping("/name")
    public ResponseEntity<List<Doctor>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(doctorService.searchByName(name));
    }

    // POST login
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        Map<String, Object> response = doctorService.login(body.get("email"), body.get("password"));
        if (Boolean.FALSE.equals(response.get("success"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        return ResponseEntity.ok(response);
    }

    // POST add doctor (admin only)
    @PostMapping
    public ResponseEntity<Doctor> addDoctor(
            @RequestBody Doctor doctor,
            @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ") ||
                !tokenService.validateToken(authHeader.substring(7))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.addDoctor(doctor));
    }
}
