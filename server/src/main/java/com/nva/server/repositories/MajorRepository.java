package com.nva.server.repositories;

import com.nva.server.entities.Faculty;
import com.nva.server.entities.Major;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MajorRepository extends JpaRepository<Major, Long> {
    Optional<Major> findByName(String name);
    List<Major> findByFaculty(Faculty faculty);
    @Query("SELECT m FROM Major m WHERE LOWER(m.name) LIKE LOWER(concat('%', :keyword, '%'))")
    Page<Major> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(m) FROM Major m WHERE LOWER(m.name) LIKE LOWER(concat('%', :keyword, '%'))")
    long countByKeyword(@Param("keyword") String keyword);
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Major m WHERE m = :major AND m.faculty = :faculty")
    boolean isMajorExistInFaculty(@Param("major") Major major, @Param("faculty") Faculty faculty);
}
