package com.nva.server.repositories;

import com.nva.server.entities.SuggestedQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SuggestedQuestionRepository extends JpaRepository<SuggestedQuestion, Long> {
    Optional<SuggestedQuestion> findByContent(String content);
    @Query("SELECT s FROM SuggestedQuestion s WHERE LOWER(s.content) LIKE LOWER(concat('%', :keyword, '%'))")
    Page<SuggestedQuestion> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(s) FROM SuggestedQuestion s WHERE LOWER(s.content) LIKE LOWER(concat('%', :keyword, '%'))")
    long countByKeyword(@Param("keyword") String keyword);
}
