package com.mediconnect.doctor.repository;

import com.mediconnect.doctor.model.Doctor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends MongoRepository<Doctor, UUID> {
    Optional<Doctor> findByEmail(String email);
}