package com.mediconnect.auth.client;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientRequest {
    @NotBlank
    @Size(max = 25)
    String firstName;

    @NotBlank
    @Size(max = 25)
    String lastName;

    @NotBlank
    @Email
    String email;

    @NotBlank
    String phone;

    @NotNull
    @Min(0)
    @Max(100)
    Integer age;
}


