package com.mediconnect.auth;


import com.mediconnect.auth.Models.Role;
import com.mediconnect.auth.Models.UserRole;
import com.mediconnect.auth.Repository.RoleRepository;
import com.mediconnect.auth.Repository.UserRepository;
import com.mediconnect.auth.Services.AuthService;
import com.mediconnect.auth.payload.request.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer {

    @Autowired
    private AuthService authService;

    @Bean
    public CommandLineRunner initializeData(RoleRepository roleRepository,
                                            UserRepository userRepository) {
        return args -> {
            if (roleRepository.count() == 0) {
                roleRepository.save(new Role(UserRole.ROLE_ADMIN));
                roleRepository.save(new Role(UserRole.ROLE_DOCTOR));
                roleRepository.save(new Role(UserRole.ROLE_PATIENT));
                System.out.println("Created default roles.");
            } else {
                System.out.println(" Roles already exist. Skipping role creation.");
            }

            if (userRepository.count() == 0) {
                SignupRequest adminUser = new SignupRequest(
                        "admin",
                        "admin@gmail.com",
                        Set.of("admin"),
                        "Admin@123"
                );

                SignupRequest doctorUser = new SignupRequest(
                        "doctor",
                        "doctor@gmail.com",
                        Set.of("doctor"),
                        "Doctor@123"
                );

                SignupRequest patientUser = new SignupRequest(
                        "patient",
                        "patient@gmail.com",
                        Set.of("patient"),
                        "Patient@123"
                );

                authService.registerUser(adminUser);
                authService.registerUser(doctorUser);
                authService.registerUser(patientUser);

                System.out.println("Default users created.");
            } else {
                System.out.println("Users already exist. Skipping user creation.");
            }



            System.out.println(" Data initialization complete!");
        };
    }
}