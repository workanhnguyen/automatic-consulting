package com.nva.server.services.impl;

import com.nva.server.constants.CustomConstants;
import com.nva.server.dtos.ChangePasswordDto;
import com.nva.server.entities.Role;
import com.nva.server.entities.User;
import com.nva.server.exceptions.PasswordException;
import com.nva.server.exceptions.UserExistedException;
import com.nva.server.exceptions.UserNotFoundException;
import com.nva.server.repositories.UserRepository;
import com.nva.server.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
    public User editUser(User user) {
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setLastModifiedDate(new Date().getTime());

            return userRepository.save(existingUser);
        } else throw new UserNotFoundException("User is not found.");
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
    public Optional<User> getUser(String email) {
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
                userRepository.save(existingUser);
            }
        } else {
            throw new UserNotFoundException("User is not found.");
        }
    }
}
