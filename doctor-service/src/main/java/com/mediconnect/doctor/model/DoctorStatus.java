package com.mediconnect.doctor.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mediconnect.doctor.exception.InvalidDoctorStatusException;
import jakarta.validation.ValidationException;
import lombok.Getter;
import lombok.Setter;

@Getter
public enum DoctorStatus {
    AVAILABLE,
    NOT_AVAILABLE,
    DISABLED;
    @JsonCreator
    public static DoctorStatus fromValue(String value) {
        if(value == null || value.isBlank()) {
            throw new InvalidDoctorStatusException("Status field is required");
        }
        for (DoctorStatus status : DoctorStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new InvalidDoctorStatusException("Invalid doctor status value " + value);
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}

