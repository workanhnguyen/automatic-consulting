package com.nva.server.repositories;

import com.nva.server.entities.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    @Query("SELECT c FROM Conversation c WHERE LOWER(c.requestText) LIKE LOWER(concat('%', :keyword, '%')) " +
            "OR LOWER(c.responseText) LIKE LOWER(concat('%', :keyword, '%')) " +
            "OR LOWER(c.user.email) LIKE LOWER(concat('%', :keyword, '%'))")
    Page<Conversation> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(c) FROM Conversation c WHERE LOWER(c.requestText) LIKE LOWER(concat('%', :keyword, '%')) " +
            "OR LOWER(c.responseText) LIKE LOWER(concat('%', :keyword, '%')) " +
            "OR LOWER(c.user.email) LIKE LOWER(concat('%', :keyword, '%'))")
    long countByKeyword(@Param("keyword") String keyword);
}
