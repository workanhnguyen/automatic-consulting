package com.nva.server.repositories;

import com.nva.server.entities.EntranceMethodGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntranceMethodGroupRepository extends JpaRepository<EntranceMethodGroup, Long> {
}
