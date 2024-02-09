package com.nva.server.repositories;

import com.nva.server.entities.Scope;
import com.nva.server.entities.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    Optional<Topic> findByName(String name);
    @Query("SELECT t FROM Topic t WHERE LOWER(t.name) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(t.description) LIKE LOWER(concat('%', :keyword, '%'))")
    Page<Topic> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(t) FROM Topic t WHERE LOWER(t.name) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(t.description) LIKE LOWER(concat('%', :keyword, '%'))")
    long countByKeyword(@Param("keyword") String keyword);
}
