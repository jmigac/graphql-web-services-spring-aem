package io.ecx.spring.graphql.webservice.repository;

import java.util.List;

import io.ecx.spring.graphql.webservice.models.Mjesto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MjestoRepozitorij extends JpaRepository<Mjesto, Long> {

    List<Mjesto> findMjestoByIsoKod(final String iso);

    Mjesto findMjestoByIsoKodAndRegija(final String isoKod, final String regija);

    Mjesto findMjestoById(final Long id);

}
