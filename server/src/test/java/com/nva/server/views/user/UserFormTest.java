package com.nva.server.views.user;

import com.nva.server.entities.Role;
import com.nva.server.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class UserFormTest {
//    private String firstName;
//    private String lastName;
//    private String email;
//    private String password;
    private List<Role> roles;
    private User user;

    @BeforeEach  
    public void setupData() {
        user = new User();
        user.setFirstName("Anh");
        user.setLastName("Nguyen");
        user.setEmail("anh@gmail.com");
        user.setRole(Role.ROLE_USER);
        user.setPassword("1234");

        roles = Arrays.stream(Role.values()).toList();
//        companies = new ArrayList<>();
//        company1 = new Company();
//        company1.setName("Vaadin Ltd");
//        company2 = new Company();
//        company2.setName("IT Mill");
//        companies.add(company1);
//        companies.add(company2);
//
//        statuses = new ArrayList<>();
//        status1 = new Status();
//        status1.setName("Status 1");
//        status2 = new Status();
//        status2.setName("Status 2");
//        statuses.add(status1);
//        statuses.add(status2);
//
//        marcUsher = new Contact();
//        marcUsher.setFirstName("Marc");
//        marcUsher.setLastName("Usher");
//        marcUsher.setEmail("marc@usher.com");
//        marcUsher.setStatus(status1);
//        marcUsher.setCompany(company2);
    }

    @Test
    public void formFieldsPopulated() {
        UserForm userForm = new UserForm(roles);
        userForm.setUser(user);

        Assertions.assertEquals("Anh", user.getFirstName());
        Assertions.assertEquals("Nguyen", user.getLastName());
        Assertions.assertEquals("anh@gmail.com", user.getEmail());
        Assertions.assertEquals("1234", user.getPassword());
        Assertions.assertEquals("ROLE_USER", user.getRole().name());
    }

    @Test
    public void saveEventHasCorrectValues() {
        User user2 = new User();
        user2.setFirstName("Anh");
        user2.setLastName("Nguyen");
        user2.setEmail("anh@gmail.com");
        user2.setPassword("1234");
        user2.setRole(Role.ROLE_USER);

        UserForm userForm = new UserForm(roles);
        userForm.setUser(user2);

        AtomicReference<User> savedUser = new AtomicReference<>(null);
        userForm.addListener(UserForm.SaveEvent.class, e -> savedUser.set(e.getUser()));

        userForm.getSaveBtn().click();
        User saved = savedUser.get();

        Assertions.assertEquals("Anh", saved.getFirstName());
        Assertions.assertEquals("Nguyen", saved.getLastName());
        Assertions.assertEquals("anh@gmail.com", saved.getEmail());
        Assertions.assertEquals("1234", saved.getPassword());
        Assertions.assertEquals("ROLE_USER", saved.getRole().name());
    }
}