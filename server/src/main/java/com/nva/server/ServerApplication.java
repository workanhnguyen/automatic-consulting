package com.nva.server;

import com.nva.server.entities.Role;
import com.nva.server.entities.User;
import com.nva.server.repositories.UserRepository;
import com.vaadin.flow.spring.annotation.EnableVaadin;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableVaadin
@RequiredArgsConstructor
public class ServerApplication implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        User adminAccount = userRepository.findByRole(Role.ROLE_ADMIN);

        if (adminAccount == null) {
            User user = new User();
            user.setEmail("admin@gmail.com");
            user.setFirstName("Admin");
            user.setLastName("Account");
            user.setRole(Role.ROLE_ADMIN);
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            userRepository.save(user);
        }

        if (userRepository.findByEmail("anh@gmail.com").isEmpty()) {
            User user2 = new User();
            user2.setEmail("anh@gmail.com");
            user2.setFirstName("Anh");
            user2.setLastName("Nguyen");
            user2.setRole(Role.ROLE_USER);
            user2.setIsEnabled(false);
            user2.setPassword(passwordEncoder.encode("1234"));
            userRepository.save(user2);
        }
    }
}
