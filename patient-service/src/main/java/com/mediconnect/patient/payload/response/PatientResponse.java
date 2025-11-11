package com.mediconnect.patient.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponse<T> {
    private String message;

    private T data;

    public PatientResponse(String message) {
        this.message = message;
    }
}
