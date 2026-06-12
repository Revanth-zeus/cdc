package com.smartclinic.service;

import com.smartclinic.model.Doctor;
import com.smartclinic.repository.AppointmentRepository;
import com.smartclinic.repository.DoctorRepository;
import com.smartclinic.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Q10 criterion 1: returns available time slots for a doctor on a given date
    public List<String> getAvailableSlots(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        List<String> allSlots = doctor.getAvailableTimes();

        // Get already booked times on that date
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        List<String> bookedTimes = appointmentRepository
                .findByDoctorIdAndDate(doctorId, start, end)
                .stream()
                .map(a -> a.getAppointmentTime().toLocalTime().toString())
                .collect(Collectors.toList());

        return allSlots.stream()
                .filter(slot -> !bookedTimes.contains(slot))
                .collect(Collectors.toList());
    }

    // Q10 criterion 2: validate doctor login credentials and return structured response
    public Map<String, Object> login(String email, String password) {
        Map<String, Object> response = new HashMap<>();
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElse(null);

        if (doctor == null || !passwordEncoder.matches(password, doctor.getPassword())) {
            response.put("success", false);
            response.put("message", "Invalid credentials");
            return response;
        }

        String token = tokenService.generateToken(email);
        response.put("success", true);
        response.put("token", token);
        response.put("doctorId", doctor.getId());
        response.put("name", doctor.getFirstName() + " " + doctor.getLastName());
        return response;
    }

    public Doctor addDoctor(Doctor doctor) {
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        return doctorRepository.save(doctor);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public List<Doctor> getDoctorsBySpeciality(String speciality) {
        return doctorRepository.findBySpecialityIgnoreCase(speciality);
    }

    public List<Doctor> searchByName(String name) {
        return doctorRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
    }
}
