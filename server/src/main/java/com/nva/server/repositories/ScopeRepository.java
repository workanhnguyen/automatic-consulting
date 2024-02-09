package com.nva.server.repositories;

import com.nva.server.entities.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScopeRepository extends JpaRepository<Scope, Long> {
    Optional<Scope> findByName(String name);
    @Query("SELECT s FROM Scope s WHERE LOWER(s.name) LIKE LOWER(concat('%', :keyword, '%'))")
    Page<Scope> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(s) FROM Scope s WHERE LOWER(s.name) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(s.description) LIKE LOWER(concat('%', :keyword, '%'))")
    long countByKeyword(@Param("keyword") String keyword);
}
