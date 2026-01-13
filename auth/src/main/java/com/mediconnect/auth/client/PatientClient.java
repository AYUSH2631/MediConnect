package com.mediconnect.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "patient-service", url = "http://localhost:8082")
public interface PatientClient {

    @PostMapping("/api/v1/patient/")
    void createPatient(@RequestBody PatientRequest request);
}
