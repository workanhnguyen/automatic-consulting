package com.nva.server.services.impl;

import com.nva.server.entities.User;
import com.nva.server.repositories.UserRepository;
import com.nva.server.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUser(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getUsers(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return userRepository.findAll();
        } else {
            return userRepository.search(searchTerm);
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
        userRepository.delete(user);
    }
}
