package com.nva.server;

import com.nva.server.entities.Role;
import com.nva.server.entities.User;
import com.nva.server.repositories.UserRepository;
import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableVaadin
public class ServerApplication implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

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

            User user2 = new User();
            user2.setEmail("anh@gmail.com");
            user2.setFirstName("Anh");
            user2.setLastName("Nguyen");
            user2.setRole(Role.ROLE_USER);
            user2.setPassword(new BCryptPasswordEncoder().encode("1234"));
            userRepository.save(user2);
        }
    }
}
