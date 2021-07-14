package io.ecx.spring.graphql.webservice.repository;

import io.ecx.spring.graphql.webservice.models.DnevnikRada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DnevnikRadaRepozitorij extends JpaRepository<DnevnikRada, Long> {
}
