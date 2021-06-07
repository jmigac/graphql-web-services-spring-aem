package io.ecx.spring.graphql.webservice.controllers;

import io.ecx.spring.graphql.webservice.models.Korisnik;
import io.ecx.spring.graphql.webservice.models.RestApiOdgovor;
import io.ecx.spring.graphql.webservice.services.KorisnikServis;
import io.ecx.spring.graphql.webservice.services.MjestoServis;
import io.ecx.spring.graphql.webservice.utils.ResponseEntityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping(path = "/api/v1/korisnici")
public class KorisnikKontroler {

    @Autowired
    private KorisnikServis servis;

    @Autowired
    private MjestoServis mjestoServis;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestApiOdgovor<Korisnik>> dohvatiSveKorisnike() {
        return ResponseEntityUtils.GetRestApiOdgovorForKorisnik(HttpStatus.OK, this.servis.dohvatiSve());
    }

    @GetMapping(path = "/{email}")
    public ResponseEntity<RestApiOdgovor<Korisnik>> dohvatiKorisnikaPoEmailu(@PathVariable final String email) {
        if (this.servis.postoji(email)) {
            return ResponseEntityUtils.GetRestApiOdgovorForKorisnik(HttpStatus.OK, this.servis.dohvatiPoEmailu(email));
        }
        return ResponseEntityUtils.GetRestApiOdgovorForKorisnik(HttpStatus.NOT_FOUND, new ArrayList<>());
    }

    @PostMapping(path = "/dodaj")
    public ResponseEntity<RestApiOdgovor<Korisnik>> dodajKorisnika(@RequestBody final Korisnik korisnik) {
        if (!this.servis.postoji(korisnik.getEmail())) {
            final Korisnik spremljeniKorisnik = this.servis.spremi(korisnik);
            return ResponseEntityUtils.GetRestApiOdgovorForKorisnik(HttpStatus.OK, spremljeniKorisnik);
        }
        return ResponseEntityUtils.GetRestApiOdgovorForKorisnik(HttpStatus.BAD_REQUEST, new ArrayList<>());
    }

    @PostMapping(path = "/dodajNovog")
    public ResponseEntity<RestApiOdgovor<Korisnik>> dodajNovogKorisnika(@RequestBody String zaglavlja) {
        final String[] polja = zaglavlja.split("&");
        if (polja.length == 4) {
            final Korisnik noviKorisnik = Korisnik
                                            .builder()
                                            .ime(new String(polja[0].strip().split("=")[1].strip().getBytes(StandardCharsets.UTF_8),
                                              StandardCharsets.UTF_8))
                                            .prezime(polja[1].strip().split("=")[1].strip())
                                            .email(polja[2].strip().split("=")[1].strip().replace("%40", "@"))
                                            .mjesto(this.mjestoServis.dohvatiMjestoPoId(Long.parseLong(polja[3].strip().split("=")[1].strip())))
                                            .build();
            return ResponseEntityUtils.GetRestApiOdgovorForKorisnik(HttpStatus.OK, this.servis.spremi(noviKorisnik));
        } else {
            return ResponseEntityUtils.GetRestApiOdgovorForKorisnik(HttpStatus.BAD_REQUEST, new ArrayList<>());
        }
    }

    @PutMapping
    public ResponseEntity<RestApiOdgovor<Korisnik>> azurirajKorisnika(@RequestBody final Korisnik korisnik) {
        if (this.servis.postoji(korisnik.getId())) {
            return ResponseEntityUtils.GetRestApiOdgovorForKorisnik(HttpStatus.OK, this.servis.azuriraj(korisnik));
        }
        return ResponseEntityUtils.GetRestApiOdgovorForKorisnik(HttpStatus.BAD_REQUEST, new ArrayList<>());
    }

    @DeleteMapping(path = "/izbrisi/{id}")
    public void izbrisiKorisnika(@PathVariable final Long id) {
        if (this.servis.nadiPoId(id).isPresent()) {
            this.servis.izbrisi(id);
        }
    }

}
