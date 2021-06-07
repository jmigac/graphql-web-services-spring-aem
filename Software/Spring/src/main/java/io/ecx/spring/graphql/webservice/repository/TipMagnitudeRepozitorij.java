package io.ecx.spring.graphql.webservice.repository;

import io.ecx.spring.graphql.webservice.models.TipMagnitude;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipMagnitudeRepozitorij extends JpaRepository<TipMagnitude, Long> {

    TipMagnitude findTipMagnitudeByTip(final String tip);

}
