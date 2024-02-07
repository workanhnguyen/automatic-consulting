package com.nva.server.repositories;

import com.nva.server.entities.EntranceScoreInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntranceScoreInformationRepository extends JpaRepository<EntranceScoreInformation, Long> {
}
