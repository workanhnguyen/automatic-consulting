package com.nva.server.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "faculties")
public class Faculty implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, length = 100)
    @NotEmpty(message = "Must not be empty")
    private String name;
    @CreatedDate
    @Column(nullable = false)
    private Long createdDate;
    @LastModifiedDate
    private Long lastModifiedDate;
    @Lob
    private String note;
    @PrePersist
    protected void onCreate() {
        this.createdDate = System.currentTimeMillis();
    }
}
