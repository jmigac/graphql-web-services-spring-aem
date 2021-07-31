package io.ecx.spring.graphql.webservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.ecx.spring.graphql.webservice.models.KorisnickaObavijest;
import io.ecx.spring.graphql.webservice.models.Korisnik;
import io.ecx.spring.graphql.webservice.models.PotresModel;

public interface KorisnickaObavijestRepozitorij extends JpaRepository<KorisnickaObavijest, Long> {

    boolean existsByPotresAndKorisnik(PotresModel potres, Korisnik korisnik);

}
