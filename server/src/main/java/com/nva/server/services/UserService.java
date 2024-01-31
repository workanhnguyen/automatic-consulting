package com.nva.server.services;

import com.nva.server.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);
//    Role saveRole(Role role);
//    void addRoleToUser(String email, String roleName);
    Optional<User> getUser(String email);
    List<User> getUsers(String searchTerm);
    Optional<User> findByEmail(String email);
    long countUsers();
    void removeUser(User user);
}
