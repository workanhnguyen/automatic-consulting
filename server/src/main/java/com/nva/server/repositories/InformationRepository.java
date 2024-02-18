package com.nva.server.repositories;

import com.nva.server.entities.Information;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InformationRepository extends JpaRepository<Information, Long> {
    Optional<Information> findByContent(String content);
    @Query("SELECT info FROM Information info WHERE LOWER(info.content) LIKE LOWER(concat('%', :keyword, '%'))")
    Page<Information> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(info) FROM Information info WHERE LOWER(info.content) LIKE LOWER(concat('%', :keyword, '%'))")
    long countByKeyword(@Param("keyword") String keyword);
}
