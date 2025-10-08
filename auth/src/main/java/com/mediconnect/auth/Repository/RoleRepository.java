package com.mediconnect.auth.Repository;

import com.mediconnect.auth.Models.Role;
import com.mediconnect.auth.Models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(UserRole name);
}