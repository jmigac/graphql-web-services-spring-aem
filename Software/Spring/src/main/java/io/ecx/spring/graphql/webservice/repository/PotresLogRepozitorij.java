package io.ecx.spring.graphql.webservice.repository;

import io.ecx.spring.graphql.webservice.models.PotresLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PotresLogRepozitorij extends JpaRepository<PotresLog, Long> {

    boolean existsPotresLogByDatum(final Date date);

}
