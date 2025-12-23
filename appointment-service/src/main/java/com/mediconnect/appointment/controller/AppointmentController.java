package com.mediconnect.appointment.controller;


import com.mediconnect.appointment.model.Appointment;
import com.mediconnect.appointment.payload.request.AppointmentRequest;
import com.mediconnect.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/create")
    public ResponseEntity<String> bookAppointment(@Valid @RequestBody AppointmentRequest appointmentRequest) {
        try {
            String result = appointmentService.bookAppointment(appointmentRequest);
            logger.info("Appointment booked successfully: {}", result);
            return ResponseEntity.ok("Appointment booked successfully. ID: " + result);
        } catch (Exception e) {
            logger.error("Error booking appointment: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Unable to book appointment.");
        }
    }


    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<?> getAppointmentsByDoctorId(@PathVariable String doctorId) {
        try {
            List<Appointment> appointments = appointmentService.getByDoctorId(doctorId);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unable to fetch appointments.");
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getAppointmentsByPatientId(@PathVariable String patientId) {
        logger.info("Fetching appointments for patient ID: {}", patientId);
        try {
            List<Appointment> appointments = appointmentService.getByPatientId(patientId);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unable to fetch appointments.");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllAppointments() {
        logger.info("Fetching all appointments.");
        try {
            List<Appointment> appointments = appointmentService.getAllAppointments();
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unable to fetch appointments.");
        }
    }

    @PutMapping
    public ResponseEntity<String> updateAppointment(@Valid @RequestBody AppointmentRequest appointmentRequest) {
        logger.info("Updating appointment: {}", appointmentRequest);
        try {
            String result = appointmentService.updateAppointment(appointmentRequest);
            logger.info("Appointment updated successfully: {}", result);
            return ResponseEntity.ok("Appointment updated successfully. ID: " + result);
        } catch (Exception e) {
            logger.error("Error updating appointment: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Unable to update appointment.");
        }
    }
}
