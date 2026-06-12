package com.smartclinic.service;

import com.smartclinic.model.Patient;
import com.smartclinic.repository.PatientRepository;
import com.smartclinic.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Patient register(Patient patient) {
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        return patientRepository.save(patient);
    }

    public Map<String, Object> login(String email, String password) {
        Map<String, Object> response = new HashMap<>();
        Patient patient = patientRepository.findByEmail(email).orElse(null);

        if (patient == null || !passwordEncoder.matches(password, patient.getPassword())) {
            response.put("success", false);
            response.put("message", "Invalid credentials");
            return response;
        }

        String token = tokenService.generateToken(email);
        response.put("success", true);
        response.put("token", token);
        response.put("patientId", patient.getId());
        response.put("name", patient.getFirstName() + " " + patient.getLastName());
        return response;
    }
}
