package com.nva.server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nva.server.entities.Role;
import com.nva.server.entities.User;
import com.nva.server.repositories.UserRepository;
import com.vaadin.flow.spring.annotation.EnableVaadin;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootApplication
@EnableVaadin
@RequiredArgsConstructor
public class ServerApplication {
    private final ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            loadUserData();
        };
    }

    private void loadUserData() {
        if (userRepository.count() == 0) {
            InputStream inputStream = getClass().getResourceAsStream("/data/users.json");
            try {
                List<User> users = objectMapper.readValue(inputStream, new TypeReference<>() {});
                for (User user : users) {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    userRepository.save(user);
                }
            } catch (IOException ignored) {}
        }
    }
}
