package com.nva.server.apis;

import com.nva.server.dtos.ChangePasswordRequest;
import com.nva.server.dtos.EditUserRequest;
import com.nva.server.dtos.UserResponse;
import com.nva.server.entities.User;
import com.nva.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin
public class UserApi {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers(null));
    }
    @PostMapping("/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PatchMapping("/updateInfo")
    public ResponseEntity<UserResponse> updateUserInfo(@RequestBody EditUserRequest user) {
        return ResponseEntity.ok().body(userService.editUserInfo(user));
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(changePasswordRequest);
        return ResponseEntity.ok(Collections.singletonMap("message", "Password has been changed successfully!"));
    }

    @PostMapping("/toggleLockAccount")
    public ResponseEntity<?> toggleLockAccount() {
        return ResponseEntity.ok(userService.toggleLockUser());
    }

    @GetMapping("/sayHello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("User hello!");
    }
}
