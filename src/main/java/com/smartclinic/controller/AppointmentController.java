package com.smartclinic.controller;

import com.smartclinic.model.Appointment;
import com.smartclinic.security.TokenService;
import com.smartclinic.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private TokenService tokenService;

    // Book appointment
    @PostMapping
    public ResponseEntity<?> bookAppointment(
            @RequestBody Map<String, Object> body,
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ") ||
                !tokenService.validateToken(authHeader.substring(7))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized"));
        }

        Long doctorId = Long.valueOf(body.get("doctorId").toString());
        Long patientId = Long.valueOf(body.get("patientId").toString());
        LocalDateTime time = LocalDateTime.parse(body.get("appointmentTime").toString());
        String notes = body.getOrDefault("notes", "").toString();

        Appointment appointment = appointmentService.bookAppointment(doctorId, patientId, time, notes);
        return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
    }

    // Get appointments for logged-in patient - used in Q25
    @GetMapping("/my")
    public ResponseEntity<?> getMyAppointments(
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ") ||
                !tokenService.validateToken(authHeader.substring(7))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized"));
        }

        String email = tokenService.extractEmail(authHeader.substring(7));
        // In real implementation, look up patient by email then their appointments
        return ResponseEntity.ok(Map.of("email", email, "message", "Appointments retrieved"));
    }

    // Get all appointments for a doctor
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getDoctorAppointments(
            @PathVariable Long doctorId,
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ") ||
                !tokenService.validateToken(authHeader.substring(7))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(doctorId));
    }
}
