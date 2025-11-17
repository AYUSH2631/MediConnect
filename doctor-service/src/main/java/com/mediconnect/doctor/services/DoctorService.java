package com.mediconnect.doctor.services;

import com.mediconnect.doctor.exception.ResourceNotFoundException;
import com.mediconnect.doctor.model.Doctor;
import com.mediconnect.doctor.model.DoctorStatus;
import com.mediconnect.doctor.repository.DoctorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DoctorService {

    private static final Logger logger = LoggerFactory.getLogger(DoctorService.class);

    @Autowired
    private DoctorRepository doctorRepository;

    public List<Doctor> getAllDoctors() {
        List<Doctor> doctorList = doctorRepository.findAll();
        logger.debug("Number of doctors fetched: {}", doctorList.size());
        return new ArrayList<>(doctorList);
    }

    public Doctor addDoctor(com.mediconnect.doctor.payload.request.Doctor doctor) throws Exception {
        Optional<Doctor> optionalDoctor = doctorRepository.findByEmail(doctor.getEmail());
        if(optionalDoctor.isPresent()) {
            logger.error("Failed to add doctor: A doctor with email {} already exists", doctor.getEmail());
            throw new Exception("A doctor with this email already exists");
        }
        UUID generatedId = UUID.randomUUID();
        Doctor doctorEntity = new Doctor(generatedId,
                doctor.getFirstName(), doctor.getLastName(),
                doctor.getEmail(), doctor.getPhone(), doctor.getSpeciality(),
                doctor.getYearsOfExperience(), DoctorStatus.fromValue(doctor.getStatus()));
        return doctorRepository.save(doctorEntity);
    }

    public Doctor getDoctorById(String id) throws ResourceNotFoundException {
        UUID uuid = UUID.fromString(id);
        Optional<Doctor> doctor = doctorRepository.findById(uuid);
        if(doctor.isEmpty()) {
            logger.error("Doctor not found with id: {}", id);
            throw new ResourceNotFoundException("doctor not found");
        }
        return doctor.get();
    }

    public Doctor getDoctorByEmail(String email) throws ResourceNotFoundException {
        Optional<Doctor> doctor = doctorRepository.findByEmail(email);
        if(doctor.isEmpty()) {
            logger.error("Doctor not found with email: {}", email);
            throw new ResourceNotFoundException("doctor not found");
        }
        return doctor.get();
    }

    public Doctor updateDoctorById(String id,com.mediconnect.doctor.payload.request.Doctor doctor) throws ResourceNotFoundException {
        UUID uuid = UUID.fromString(id);

        Optional<Doctor> existingDoctor = doctorRepository.findById(uuid);
        if (existingDoctor.isEmpty()) {
            throw new ResourceNotFoundException("Doctor not found with id: " + id);
        }

        Doctor doctorToUpdate = existingDoctor.get();

        doctorToUpdate.setId(uuid);
        doctorToUpdate.setFirstName(doctor.getFirstName());
        doctorToUpdate.setLastName(doctor.getLastName());
        doctorToUpdate.setEmail(doctor.getEmail());
        doctorToUpdate.setPhone(doctor.getPhone());
        doctorToUpdate.setSpeciality(doctor.getSpeciality());
        doctorToUpdate.setYearsOfExperience(doctor.getYearsOfExperience());
        doctorToUpdate.setStatus(DoctorStatus.fromValue(doctor.getStatus()));

        return doctorRepository.save(doctorToUpdate);
    }


    public void deleteDoctorById(String id) throws ResourceNotFoundException {
        UUID uuid = UUID.fromString(id);
        Optional<Doctor> optionalDoctor = doctorRepository.findById(uuid);
        if (optionalDoctor.isEmpty()) {
            throw new ResourceNotFoundException("doctor not found with id: " + id);
        }
        Doctor doctor = optionalDoctor.get();
        doctor.setStatus(DoctorStatus.fromValue("DISABLED"));
        doctorRepository.save(doctor);
        logger.info("Doctor deleted successfully with id: {}", id);
    }

}



