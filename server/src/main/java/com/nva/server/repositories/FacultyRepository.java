package com.nva.server.repositories;

import com.nva.server.entities.Faculty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Optional<Faculty> findByName(String name);
    @Query("SELECT f FROM Faculty f WHERE LOWER(f.name) LIKE LOWER(concat('%', :keyword, '%'))")
    Page<Faculty> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(f) FROM Faculty f WHERE LOWER(f.name) LIKE LOWER(concat('%', :keyword, '%'))")
    long countByKeyword(@Param("keyword") String keyword);
}
