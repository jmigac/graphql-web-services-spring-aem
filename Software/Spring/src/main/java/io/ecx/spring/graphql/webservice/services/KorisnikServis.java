package io.ecx.spring.graphql.webservice.services;

import io.ecx.spring.graphql.webservice.helpers.DnevnikRadaHelper;
import io.ecx.spring.graphql.webservice.models.DnevnikRada;
import io.ecx.spring.graphql.webservice.models.Korisnik;
import io.ecx.spring.graphql.webservice.repository.KorisnikRepositorij;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class KorisnikServis {

    @Autowired
    private KorisnikRepositorij repositorij;

    @Autowired
    private DnevnikRadaServis dnevnikRadaServis;

    public List<Korisnik> dohvatiSve() {
        return this.repositorij.findAll();
    }

    public Korisnik spremi(final Korisnik korisnik) {
        this.dnevnikRadaServis.spremi(
          DnevnikRada.builder().radnja(DnevnikRadaHelper.AKCIJA_UPISA_KORISNIKA_U_BAZU).vrijemeRadnje(new Date()).build());
        return this.repositorij.save(korisnik);
    }

    public boolean postoji(final String email) {
        this.dnevnikRadaServis.spremi(
          DnevnikRada.builder().radnja(DnevnikRadaHelper.PROVJERA_POSTOJANOSTI_KORISNIKA).vrijemeRadnje(new Date()).build());
        return this.repositorij.findKorisnikByEmail(email) != null;
    }

    public boolean postoji(final Long id) {
        this.dnevnikRadaServis.spremi(
          DnevnikRada.builder().radnja(DnevnikRadaHelper.PROVJERA_POSTOJANOSTI_KORISNIKA).vrijemeRadnje(new Date()).build());
        return this.repositorij.findById(id) != null;
    }

    public Korisnik dohvatiPoEmailu(final String email) {
        this.dnevnikRadaServis.spremi(
          DnevnikRada.builder().radnja(DnevnikRadaHelper.DOHVACANJE_KORISNIKA_PO_EMAILU).vrijemeRadnje(new Date()).build());
        return this.repositorij.findKorisnikByEmail(email);
    }

    public void izbrisi(final Long id) {
        this.repositorij.deleteById(id);
    }

    public Korisnik azuriraj(final Korisnik korisnik) {
        return this.repositorij.save(korisnik);
    }

    public Optional<Korisnik> nadiPoId(final Long id) {
        return this.repositorij.findById(id);
    }

    public List<Korisnik> nadiKorisnikePoIsoKodu(final String isoKod){
        return this.repositorij.findKorisniksByMjesto_IsoKod(isoKod);
    }

}
