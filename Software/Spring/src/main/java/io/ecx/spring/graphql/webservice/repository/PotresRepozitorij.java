package io.ecx.spring.graphql.webservice.repository;

import io.ecx.spring.graphql.webservice.models.PotresModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PotresRepozitorij extends JpaRepository<PotresModel, Long> {

    boolean existsPotresModelById(final String id);

}
