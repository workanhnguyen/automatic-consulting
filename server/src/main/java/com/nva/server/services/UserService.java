package com.nva.server.services;

import com.nva.server.dtos.ChangePasswordDto;
import com.nva.server.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);
    User editUser(User user);
    void toggleLockUser(String email);
    Optional<User> getUser(String email);
    List<User> getUsers(String searchTerm);
    Optional<User> findByEmail(String email);
    long countUsers();
    void removeUser(User user);
    void changePassword(String email, ChangePasswordDto changePasswordDto);
}
