package com.nva.server.repositories;

import com.nva.server.entities.Action;
import com.nva.server.entities.EntranceMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntranceMethodRepository extends JpaRepository<EntranceMethod, Long> {
    Optional<EntranceMethod> findByName(String name);
    @Query("SELECT em FROM EntranceMethod em WHERE LOWER(em.name) LIKE LOWER(concat('%', :keyword, '%'))")
    Page<Action> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(em) FROM EntranceMethod em WHERE LOWER(em.name) LIKE LOWER(concat('%', :keyword, '%'))")
    long countByKeyword(@Param("keyword") String keyword);
}
