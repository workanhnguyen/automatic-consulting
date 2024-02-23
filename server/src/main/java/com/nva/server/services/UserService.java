package com.nva.server.services;

import com.nva.server.dtos.ChangePasswordDto;
import com.nva.server.dtos.ChangePasswordRequest;
import com.nva.server.dtos.EditUserRequest;
import com.nva.server.dtos.UserResponse;
import com.nva.server.entities.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);
    void editUser(User user);
    UserResponse editUserInfo(EditUserRequest user);
    void updateAvatar(User user);
    String updateAvatar(String avatarBase64, User user);
    void toggleLockUser(String email);
    UserResponse toggleLockUser();
    Optional<User> getUserByEmail(String email);
    List<User> getUsers(Map<String, Object> params);
    long getUserCount(Map<String, Object> params);
    Optional<User> findByEmail(String email);
    long countUsers();
    void removeUser(User user);
    void changePassword(String email, ChangePasswordDto changePasswordDto);
    void changePassword(ChangePasswordRequest request);
    void deleteUser();
    UserResponse getProfile();
}
