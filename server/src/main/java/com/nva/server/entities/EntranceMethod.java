package com.nva.server.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "entrance_methods")
public class EntranceMethod implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, length = 500)
    private String name;
    @CreatedDate
    @Column(nullable = false)
    private Long createdDate;
    @LastModifiedDate
    private Long lastModifiedDate;
    @Lob
    private String note;
    @ManyToOne
    @JoinColumn(name = "entrance_method_group_id", nullable = false)
    private EntranceMethodGroup entranceMethodGroup;

    @PrePersist
    protected void onCreate() {
        this.createdDate = System.currentTimeMillis();
    }
}
