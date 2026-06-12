package com.smartclinic.service;

import com.smartclinic.model.Appointment;
import com.smartclinic.model.Doctor;
import com.smartclinic.model.Patient;
import com.smartclinic.repository.AppointmentRepository;
import com.smartclinic.repository.DoctorRepository;
import com.smartclinic.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    // Q6 criterion 1: booking method that saves an appointment
    public Appointment bookAppointment(Long doctorId, Long patientId,
                                       LocalDateTime appointmentTime, String notes) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentTime(appointmentTime);
        appointment.setNotes(notes);
        appointment.setStatus("SCHEDULED");

        return appointmentRepository.save(appointment);
    }

    // Q6 criterion 2: retrieve appointments for a doctor on a specific date
    public List<Appointment> getAppointmentsByDoctorAndDate(Long doctorId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        return appointmentRepository.findByDoctorIdAndDate(doctorId, start, end);
    }

    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }
}
