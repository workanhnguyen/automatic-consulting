package com.nva.server.repositories;

import com.nva.server.entities.Role;
import com.nva.server.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findByRole(Role role);

    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(concat('%', :keyword, '%')) " +
            "OR LOWER(u.lastName) LIKE LOWER(concat('%', :keyword, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(concat('%', :keyword, '%'))")
    Page<User> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u WHERE LOWER(u.firstName) LIKE LOWER(concat('%', :keyword, '%')) " +
            "OR LOWER(u.lastName) LIKE LOWER(concat('%', :keyword, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(concat('%', :keyword, '%'))")
    long countByKeyword(@Param("keyword") String keyword);
}
