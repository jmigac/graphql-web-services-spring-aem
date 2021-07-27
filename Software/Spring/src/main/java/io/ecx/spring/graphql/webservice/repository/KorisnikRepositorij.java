package io.ecx.spring.graphql.webservice.repository;

import java.util.List;

import io.ecx.spring.graphql.webservice.models.Korisnik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KorisnikRepositorij extends JpaRepository<Korisnik, Long> {

    Korisnik findKorisnikByEmail(final String email);

    Korisnik findKorisnikById(final Long id);

    List<Korisnik> findKorisniksByMjesto_IsoKod(final String isoKod);

}
