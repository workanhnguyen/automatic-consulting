package com.nva.server.repositories;

import com.nva.server.entities.Action;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {
    Optional<Action> findByName(String name);
    @Query("SELECT a FROM Action a WHERE LOWER(a.name) LIKE LOWER(concat('%', :keyword, '%'))")
    Page<Action> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(a) FROM Action a WHERE LOWER(a.name) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(a.description) LIKE LOWER(concat('%', :keyword, '%'))")
    long countByKeyword(@Param("keyword") String keyword);
}
