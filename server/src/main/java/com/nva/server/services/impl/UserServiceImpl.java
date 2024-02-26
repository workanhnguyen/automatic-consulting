package com.nva.server.services.impl;

import com.cloudinary.Cloudinary;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nva.server.constants.CustomConstants;
import com.nva.server.dtos.ChangePasswordDto;
import com.nva.server.dtos.ChangePasswordRequest;
import com.nva.server.dtos.EditUserRequest;
import com.nva.server.dtos.UserResponse;
import com.nva.server.entities.Role;
import com.nva.server.entities.User;
import com.nva.server.exceptions.*;
import com.nva.server.repositories.UserRepository;
import com.nva.server.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public User saveUser(User user) {
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        if (optionalUser.isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
        throw new UserExistedException("Email is already taken.");
    }

    @Override
    public void editUser(User user) {
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setAvatarFile(user.getAvatarFile());
            existingUser.setAvatarBase64(user.getAvatarBase64());
            existingUser.setLastModifiedDate(new Date().getTime());

            updateAvatar(existingUser);
        } else throw new UserNotFoundException("User is not found.");
    }

    @Override
    public UserResponse editUserInfo(EditUserRequest user) {
        Optional<User> optionalUser = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (optionalUser.isPresent()) {
            try {
                optionalUser.get().setFirstName(user.getFirstName());
                optionalUser.get().setLastName(user.getLastName());
                optionalUser.get().setLastModifiedDate(new Date().getTime());

                return getUserResponse(optionalUser.get());
            } catch (Exception ex) {
                throw new DatabaseException("Update user failed.");
            }
        } else {
            throw new UserNotFoundException("User is not found.");
        }
    }

    private static UserResponse getUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setCreatedDate(user.getCreatedDate());
        response.setLastModifiedDate(user.getLastModifiedDate());
        response.setIsEnabled(user.isEnabled());
        response.setAvatarLink(user.getAvatarLink());
        return response;
    }

    @Override
    public void updateAvatar(User user) {
        if (user.getAvatarFile() != null && !user.getAvatarFile().isEmpty()) {
            try {
                String publicId = "user_avatar_" + user.getEmail();

                Map<String, Object> params = new HashMap<>();
                params.put("resource_type", "image");
                params.put("folder", "user");
                params.put("public_id", publicId);
                Map<?, ?> uploadResult = this.cloudinary.uploader().upload(user.getAvatarFile().getBytes(), params);

                // Extract the secure URL of the uploaded image
                user.setAvatarLink(uploadResult.get("secure_url").toString());
                user.setLastModifiedDate(new Date().getTime());

                // Delete old avatar only if upload was successful
                if (!uploadResult.isEmpty()) {
                    this.cloudinary.uploader().destroy(publicId, null);
                }
            } catch (Exception e) {
                throw new DatabaseException("Edit user failed");
            }
        }

        if (user.getAvatarBase64() != null && !user.getAvatarBase64().isEmpty()) {
            try {
                String publicId = "user_avatar_" + user.getEmail();

                Map<String, Object> params = new HashMap<>();
                params.put("resource_type", "image");
                params.put("folder", "user"); // Optional folder to organize your images
                params.put("public_id", publicId);
                Map<?, ?> uploadResult = this.cloudinary.uploader().upload(user.getAvatarBase64(), params);

                // Extract the secure URL of the uploaded image
                user.setAvatarLink(uploadResult.get("secure_url").toString());
                user.setLastModifiedDate(new Date().getTime());

                // Delete old avatar only if upload was successful
                if (!uploadResult.isEmpty()) {
                    this.cloudinary.uploader().destroy(publicId, null);
                }
            } catch (Exception e) {
                throw new DatabaseException("Update avatar failed.");
            }
        }

    }

    @Override
    public String updateAvatar(String avatarBase64, User user) {
        if (avatarBase64 != null && !avatarBase64.isEmpty()) {
            try {
                String publicId = "user_avatar_" + user.getEmail();

                Map<String, Object> params = new HashMap<>();
                params.put("resource_type", "image");
                params.put("folder", "user"); // Optional folder to organize your images
                params.put("public_id", publicId);
                Map<?, ?> uploadResult = this.cloudinary.uploader().upload(avatarBase64, params);

                // Delete old avatar only if upload was successful
                if (!uploadResult.isEmpty())
                    this.cloudinary.uploader().destroy(publicId, null);

                return uploadResult.get("secure_url").toString();
            } catch (Exception e) {
                throw new DatabaseException("Edit user failed");
            }
        }
        return user.getAvatarLink();
    }


    @Override
    public void toggleLockUser(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            if (!optionalUser.get().getRole().equals(Role.ROLE_ADMIN)) {
                User existingUser = optionalUser.get();
                existingUser.setIsEnabled(!existingUser.isEnabled());
                userRepository.save(existingUser);
            } else throw new UserNotFoundException("You cannot lock ADMIN account.");
        } else throw new UserNotFoundException("User is not found.");
    }

    @Override
    public UserResponse toggleLockUser() {
        Optional<User> optionalUser = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (optionalUser.isPresent()) {
            if (!optionalUser.get().getRole().equals(Role.ROLE_ADMIN)) {
                optionalUser.get().setIsEnabled(!optionalUser.get().isEnabled());

                return getUserResponse(optionalUser.get());
            } else throw new UserNotFoundException("You cannot lock ADMIN account.");
        } else throw new UserNotFoundException("User is not found.");
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getUsers(Map<String, Object> params) {
        String keyword = (String) params.get("keyword");
        int pageNumber = (int) params.getOrDefault("pageNumber", 0);
        int pageSize = (int) params.getOrDefault("pageSize", CustomConstants.USER_PAGE_SIZE);

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));

        if (keyword == null) {
            return userRepository.findAll(pageable).getContent();
        } else {
            return userRepository.search(keyword, pageable).getContent();
        }
    }

    @Override
    public long getUserCount(Map<String, Object> params) {
        String keyword = (String) params.get("keyword");

        if (keyword == null) {
            return userRepository.count();
        } else {
            return userRepository.countByKeyword(keyword);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public long countUsers() {
        return userRepository.count();
    }

    @Override
    public void removeUser(User user) {
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        if (optionalUser.isPresent()) {
            if (optionalUser.get().getRole().equals(Role.ROLE_ADMIN))
                throw new UserNotFoundException("You cannot delete ADMIN account.");
            userRepository.delete(user);
        } else throw new UserNotFoundException("User is not found.");
    }

    @Override
    public void changePassword(String email, ChangePasswordDto changePasswordDto) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            if (!passwordEncoder.matches(changePasswordDto.getPassword(), optionalUser.get().getPassword())) {
                throw new PasswordException("Current password is incorrect.");
            }
            if (passwordEncoder.matches(changePasswordDto.getNewPassword(), optionalUser.get().getPassword())) {
                throw new PasswordException("New password should not be equal to the old password.");
            } else {
                User existingUser = optionalUser.get();
                existingUser.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
                existingUser.setLastModifiedDate(new Date().getTime());
                userRepository.save(existingUser);
            }
        } else {
            throw new UserNotFoundException("User is not found.");
        }
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (optionalUser.isPresent()) {
            if (!passwordEncoder.matches(request.getOldPassword(), optionalUser.get().getPassword())) {
                throw new PasswordException("Mật khẩu hiện tại không đúng.");
            }
            if (passwordEncoder.matches(request.getNewPassword(), optionalUser.get().getPassword())) {
                throw new PasswordException("Mật khẩu mới phải khác mật khẩu cũ.");
            } else {
                User existingUser = optionalUser.get();
                existingUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
                existingUser.setLastModifiedDate(new Date().getTime());
            }
        } else {
            throw new UserNotFoundException("Người dùng không tồn tại.");
        }
    }

    @Override
    public void deleteUser() {
        Optional<User> optionalUser = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        if (optionalUser.isPresent()) {
            try {
                userRepository.delete(optionalUser.get());
            } catch (Exception ex) {
                throw new CommonException("Delete account failed.");
            }
        } else throw new UserNotFoundException("User is not found.");
    }

    @Override
    public UserResponse getProfile() {
        User existingUser = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException("User is not found."));;

            UserResponse response = new UserResponse();
            response.setEmail(existingUser.getEmail());
            response.setFirstName(existingUser.getFirstName());
            response.setLastName(existingUser.getLastName());
            response.setAvatarLink(existingUser.getAvatarLink());
            response.setIsEnabled(existingUser.getIsEnabled());
            response.setCreatedDate(existingUser.getCreatedDate());
            response.setLastModifiedDate(existingUser.getLastModifiedDate());

            return response;
    }
}
