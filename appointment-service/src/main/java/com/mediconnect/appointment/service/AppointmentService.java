package com.mediconnect.appointment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mediconnect.appointment.exception.ResourceNotFoundException;
import com.mediconnect.appointment.model.Appointment;
import com.mediconnect.appointment.model.AppointmentStatus;
import com.mediconnect.appointment.model.Doctor;
import com.mediconnect.appointment.model.Patient;
import com.mediconnect.appointment.payload.request.AppointmentRequest;
import com.mediconnect.appointment.payload.response.GenericResponse;
import com.mediconnect.appointment.repository.AppointmentRepository;
import jakarta.websocket.SendResult;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Service
public class AppointmentService {

    @Value("${app.environment}")
    private String environment;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${doctor.service.url}")
    private String doctorServiceUrl;

    @Value("${patient.service.url}")
    private String patientServiceUrl;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topic.name}")
    private String topicName;

    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    public String bookAppointment(AppointmentRequest appointment) {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            Doctor doctor = fetchDoctorDetails(appointment.getDoctorId().toString());
            logger.info("Fetched doctor details inside book appointment: {}", objectMapper.writeValueAsString(doctor));

            Patient patient = fetchPatientDetails(appointment.getPatientId().toString());
            logger.info("Fetched patient details inside book appointment: {}", objectMapper.writeValueAsString(patient));

            UUID generatedId = UUID.randomUUID();
            Appointment newAppointment = new Appointment();
            newAppointment.setId(generatedId);
            newAppointment.setDoctor(doctor);
            newAppointment.setPatient(patient);
            newAppointment.setAppointmentTime(appointment.getAppointmentTime());
            newAppointment.setDoctorComments(appointment.getDoctorComments());
            newAppointment.setNotes(appointment.getNotes());
            newAppointment.setStatus(AppointmentStatus.PENDING);

            String appointmentId = String.valueOf(appointmentRepository.save(newAppointment).getId());


            String appointmentJson = objectMapper.writeValueAsString(newAppointment);
            sendEventToKafka(appointmentJson);
            return appointmentId;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error booking appointment: " + e.getMessage());
        }
    }

    public List<Appointment> getByDoctorId(String doctorId) {
        return appointmentRepository.findByDoctorId(doctorId, Sort.by(Sort.Direction.ASC, "appointmentTime"));
    }

    public List<Appointment> getByPatientId(String patientId) {
        return appointmentRepository.findByPatientId(patientId, Sort.by(Sort.Direction.ASC, "appointmentTime"));
    }


    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAllByOrderByAppointmentTimeAsc();
    }

    public String updateAppointment(AppointmentRequest appointment) {
        try {
            Appointment existingAppointment = appointmentRepository.findById(UUID.fromString(appointment.getId()))
                    .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + appointment.getId()));

            Map<String, Object> doctor = restTemplate.getForObject(doctorServiceUrl + "/" + appointment.getDoctorId(), Map.class);
            if (doctor == null || doctor.isEmpty()) {
                throw new ResourceNotFoundException("Doctor not found with ID: " + appointment.getDoctorId());
            }

\            Map<String, Object>  patient = restTemplate.getForObject(patientServiceUrl + "/" + appointment.getPatientId(), Map.class);
            if (patient == null || patient.isEmpty()) {
                throw new ResourceNotFoundException("Patient not found with ID: " + appointment.getPatientId());
            }

            Map<String, Object> doctorData = (Map<String, Object>) doctor.get("data");

            Doctor doctor1 = new Doctor();
            doctor1.setId((String) doctorData.get("id"));
            doctor1.setFirstName((String) doctorData.get("firstName"));
            doctor1.setLastName((String) doctorData.get("lastName"));
            doctor1.setEmail((String) doctorData.get("email"));
            doctor1.setPhone((String) doctorData.get("phone"));
            doctor1.setSpeciality((String) doctorData.get("speciality"));
            doctor1.setYearsOfExperience((Integer) doctorData.get("yearsOfExperience"));
            doctor1.setStatus((String) doctorData.get("status"));


            Map<String, Object> patientData = (Map<String, Object>) patient.get("data");

            Patient patient1 = new Patient();
            patient1.setId((String) patientData.get("id"));
            patient1.setFirstName((String) patientData.get("firstName"));
            patient1.setLastName((String) patientData.get("lastName"));
            patient1.setEmail((String) patientData.get("email"));
            patient1.setPhone((String) patientData.get("phone"));
            patient1.setAge((Integer) patientData.get("age"));


            existingAppointment.setDoctor(doctor1);
            existingAppointment.setPatient(patient1);
            existingAppointment.setAppointmentTime(appointment.getAppointmentTime());
            existingAppointment.setNotes(appointment.getNotes());
            existingAppointment.setDoctorComments(appointment.getDoctorComments());
            existingAppointment.setStatus(AppointmentStatus.fromValue(appointment.getStatus()));

            String idResponse = String.valueOf(appointmentRepository.save(existingAppointment).getId());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String appointmentJson = objectMapper.writeValueAsString(existingAppointment);
            sendEventToKafka(appointmentJson);

            return idResponse;


        } catch (Exception e) {
            throw new ResourceNotFoundException("Error updating appointment: " + e.getMessage());
        }
    }

    private void sendEventToKafka(String appointmentJson) {
        CompletableFuture<SendResult<String, String>> completableFuture = kafkaTemplate.send(topicName, appointmentJson);

        completableFuture.whenComplete((result, exception) -> {
            if (exception == null) {
                RecordMetadata metadata = result.getRecordMetadata();
                logger.info("Message sent successfully to topic: {}", topicName);
                logger.info("Partition: {}, Offset: {}", metadata.partition(), metadata.offset());
            } else {
                logger.error("Failed to send message to topic: {}", topicName);
                logger.error(exception.getMessage());
            }
        });
    }

    private Doctor fetchDoctorDetails(String doctorId) {
        if (isDevelopmentEnvironment()) {
            return new Doctor(
                    doctorId,
                    "John",
                    "Doe",
                    "doctorhungrycoders@gmail.com",
                    "1234567890",
                    "Cardiology",
                    10,
                    "ACTIVE"
            );
        }

        ResponseEntity<GenericResponse<Doctor>> responseEntity = restTemplate.exchange(
                doctorServiceUrl + "/" + doctorId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<GenericResponse<Doctor>>() {}
        );

        GenericResponse<Doctor> getDoctorResponse = responseEntity.getBody();

        if (getDoctorResponse != null && getDoctorResponse.getData() != null) {
            return getDoctorResponse.getData();
        }

        throw new ResourceNotFoundException("Doctor not found with ID: " + doctorId);

    }

    private Patient fetchPatientDetails(String patientId) {
        if (isDevelopmentEnvironment()) {
            return new Patient(
                    patientId,
                    "Jane",
                    "Smith",
                    "jane.smith@example.com",
                    "0987654321",
                    30
            );
        }

        ResponseEntity<GenericResponse<Patient>> responseEntity = restTemplate.exchange(
                patientServiceUrl + "/" + patientId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<GenericResponse<Patient>>() {}
        );

        GenericResponse<Patient> getDoctorResponse = responseEntity.getBody();

        if (getDoctorResponse != null && getDoctorResponse.getData() != null) {
            return getDoctorResponse.getData();
        }

        throw new ResourceNotFoundException("Doctor not found with ID: " + patientId);
    }

    private boolean isDevelopmentEnvironment() {
        return "dev".equalsIgnoreCase(environment);
    }
}
