package com.mediconnect.appointment.payload.request;

import com.mediconnect.appointment.model.AppointmentStatus;
import com.mediconnect.appointment.utils.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppointmentRequest implements Serializable {

    private String id;

    @NotNull(message = "Doctor ID must not be null")
    private UUID doctorId;


    @NotNull(message = "Patient ID must not be null")
    private UUID patientId;


    @NotNull(message = "Appointment time must not be null")
    private LocalDateTime appointmentTime;


    @NotNull(message = "Status must be provided")
    @ValidEnum(message = "Status must be valid", enumClass = AppointmentStatus.class)
    private String status;

    @NotBlank(message = "Notes must not be blank")
    @Size(max = 200, message = "Notes must be 200 characters or less")
    private String notes;

    @Size(max = 200, message = "Doctor comments must be 200 characters or less")
    private String doctorComments;
}
