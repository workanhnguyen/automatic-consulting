package com.nva.server.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "suggested_questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuggestedQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, length = 500)
    private String content;
    @Lob
    private String note;
    @CreatedDate
    private Long createdDate;
    @LastModifiedDate
    private Long lastModifiedDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = System.currentTimeMillis();
    }
}
