package com.mediconnect.appointment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Patient {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Integer age;
}