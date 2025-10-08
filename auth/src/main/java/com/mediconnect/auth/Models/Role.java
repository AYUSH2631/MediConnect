package com.mediconnect.auth.Models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

    @Entity
    @Table(name = "roles")
    public class Role {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private String id;

        @Enumerated(EnumType.STRING)
        @Column(length = 30, nullable = false, unique = true)
        private UserRole name;

        @ManyToMany(mappedBy = "roles")
        private Set<User> users = new HashSet<>();

        public Role() {}

        public Role(UserRole name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }

        public UserRole getName() {
            return name;
        }
        public void setName(UserRole name) {
            this.name = name;
        }

        public Set<User> getUsers() {
            return users;
        }
        public void setUsers(Set<User> users) {
            this.users = users;
        }

}
