package com.mediconnect.appointment.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mediconnect.appointment.exception.InvalidAppointmentStatusException;

public enum AppointmentStatus {
    PENDING,
    CONFIRMED,
    REJECTED,
    COMPLETED;


    @JsonCreator
    public static AppointmentStatus fromValue(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidAppointmentStatusException("Status field is required");
        }
        for (AppointmentStatus status : AppointmentStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new InvalidAppointmentStatusException("Invalid appointment status value: " + value);
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}