package com.nva.server.repositories;

import com.nva.server.entities.EntranceMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntranceMethodRepository extends JpaRepository<EntranceMethod, Long> {
}
