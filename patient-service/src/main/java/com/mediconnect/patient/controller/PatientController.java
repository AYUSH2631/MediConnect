package com.mediconnect.patient.controller;

import com.mediconnect.patient.exception.ResourceNotFoundException;
import com.mediconnect.patient.model.Patient;
import com.mediconnect.patient.payload.response.PatientResponse;
import com.mediconnect.patient.service.PatientService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping("/api/v1/patient")
public class PatientController {

    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);

    @Autowired
    private PatientService patientService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable String id) throws ResourceNotFoundException {
        try {
            Patient patient = patientService.getPatientById(id);
            return ResponseEntity.status(200).body(new PatientResponse<>("Patient fetched successfully", patientService.getPatientById(id)));
        } catch (Exception e) {
            String errorMessage = "Error fetching patient's details";
            if (e.getMessage() != null && !e.getMessage().isBlank()) {
                errorMessage += ": " + e.getMessage();
            }
            return ResponseEntity.status(500).body(new PatientResponse<>(errorMessage));
        }

    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getPatientByEmail(@PathVariable String email) throws ResourceNotFoundException {
        try {
            Patient patient = patientService.getPatientByEmail(email);
            return ResponseEntity.status(200).body(new PatientResponse<>("Patient fetched successfully", patient));
        } catch (Exception e) {
            logger.error("Error fetching patient with email");
            String errorMessage = "Error fetching patient's details";
            if (e.getMessage() != null && !e.getMessage().isBlank()) {
                errorMessage += ": " + e.getMessage();
            }
            return ResponseEntity.status(500).body(new PatientResponse<>(errorMessage));
        }

    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPatients() throws Exception {
        try {
            List<Patient> patients = patientService.getAllPatients();
            return ResponseEntity.status(200).body(new PatientResponse<>("Fetched patients successfully", patients));
        } catch (Exception e) {
            logger.error("Error fetching patients");
            String errorMessage = "Error fetching patients";
            if (e.getMessage() != null && !e.getMessage().isBlank()) {
                errorMessage += ": " + e.getMessage();
            }
            return ResponseEntity.status(500).body(new PatientResponse<>(errorMessage));
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> savePatient(@Valid @RequestBody Patient patient) throws Exception {
        try {
            Patient savedPatient = patientService.addPatient(patient);
            return ResponseEntity.status(200).body(new PatientResponse<>("Patient saved successfully", savedPatient));
        } catch(Exception e) {
            return ResponseEntity.status(500).body(new PatientResponse<>("Error saving patient"));
        }

    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePatient(@Valid @RequestBody Patient patient, @PathVariable String id) throws Exception {
        try {
            Patient updatedPatient = patientService.updatePatientById(id, patient);
            return ResponseEntity.status(200).body(new PatientResponse<>("Patient updated successfully", updatedPatient));
        } catch(Exception e) {
            return ResponseEntity.status(500).body(new PatientResponse<>("Error updating patient"));
        }

    }

}
