package com.mediconnect.patient.repository;

import com.mediconnect.patient.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends MongoRepository<Patient, UUID> {
    Optional<Patient> findByEmail(String email);
}
