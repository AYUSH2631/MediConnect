package com.mediconnect.patient.payload.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Data
public class patient implements Serializable {

    @NotBlank
    @Size(max = 25, message = "must be 25 characters or less")
    private String firstName;

    @NotBlank
    @Size(max = 25, message = "must be 25 characters or less")
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phone;

    @NotNull
    @Min(value = 0, message = "age can't be below 0")
    @Max(value = 100, message = "age can't be above 100")
    private Integer age;
}
