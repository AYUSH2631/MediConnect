package com.mediconnect.auth.Repository;

import com.mediconnect.auth.Models.Role;
import com.mediconnect.auth.Models.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(UserRole name);
}