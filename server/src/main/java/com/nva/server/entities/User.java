package com.nva.server.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements Serializable, UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 20) // conditions for database
    @NotEmpty(message = "Must not be empty") // conditions for checking in backend - Neither null nor empty string
    private String firstName;

    @Column(nullable = false, length = 50)
    @NotEmpty(message = "Must not be empty")
    private String lastName;

    @Column(nullable = false, unique = true, length = 50)
    @Email(message = "Email is invalid")
    @NotEmpty(message = "Must not be empty")
    private String email;

    @Column(nullable = false)
    @Size(min = 4, message = "Password must be at least 4 characters long")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.ROLE_USER;

    @CreatedDate
    @Column(nullable = false)
    private Long createdDate;

    @LastModifiedDate
    private Long lastModifiedDate;

    @Column(nullable = false)
    private Boolean isEnabled = true;

    @Lob
    private String note;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Conversation> conversation;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    // Autofill created date when user is created
    @PrePersist
    protected void onCreate() {
        this.createdDate = System.currentTimeMillis();
    }
}
