package com.mediconnect.patient.service;


import com.mediconnect.patient.exception.ResourceNotFoundException;
import com.mediconnect.patient.model.Patient;
import com.mediconnect.patient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PatientService {

    private static final Logger logger = LoggerFactory.getLogger(PatientService.class);

    @Autowired
    private PatientRepository patientRepository;

    public List<Patient> getAllPatients() {
        List<Patient> patientList = patientRepository.findAll();
        return new ArrayList<>(patientList);
    }

    public Patient addPatient(Patient patient) throws Exception {
        Optional<Patient> optionalPatient = patientRepository.findByEmail(patient.getEmail());
        if(optionalPatient.isPresent()) {
            throw new Exception("A patient with this email already exists");
        }
        UUID generatedId = UUID.randomUUID();
        Patient patientEntity = new Patient(generatedId,
                patient.getFirstName(), patient.getLastName(),
                patient.getEmail(), patient.getPhone(), patient.getAge());
        return patientRepository.save(patientEntity);
    }

    public Patient getPatientById(String id) throws ResourceNotFoundException {
        Optional<Patient> patient = patientRepository.findById(UUID.fromString(id));
        if(patient.isEmpty()) {
            throw new ResourceNotFoundException("patient not found");
        }
        return patient.get();
    }

    public Patient getPatientByEmail(String email) throws ResourceNotFoundException {
        Optional<Patient> patient = patientRepository.findByEmail(email);
        if(patient.isEmpty()) {
            logger.error("Doctor not found with email");
            throw new ResourceNotFoundException("patient not found");
        }
        return patient.get();
    }

    public Patient updatePatientById(String id,Patient patient) throws ResourceNotFoundException {
        UUID uuid = UUID.fromString(id);

        Optional<Patient> existingDoctor = patientRepository.findById(uuid);
        if (existingDoctor.isEmpty()) {
            throw new ResourceNotFoundException("Doctor not found with id: " + id);
        }

        Patient patientToUpdate = existingDoctor.get();

        patientToUpdate.setId(uuid);
        patientToUpdate.setFirstName(patient.getFirstName());
        patientToUpdate.setLastName(patient.getLastName());
        patientToUpdate.setEmail(patient.getEmail());
        patientToUpdate.setPhone(patient.getPhone());
        patientToUpdate.setAge(patient.getAge());

        return patientRepository.save(patientToUpdate);
    }


    public void deleteDoctorById(String id) throws ResourceNotFoundException {
        UUID uuid = UUID.fromString(id);
        if (!patientRepository.existsById(uuid)) {
            throw new ResourceNotFoundException("patient not found with id: " + id);
        }
        patientRepository.deleteById(uuid);
    }

}
