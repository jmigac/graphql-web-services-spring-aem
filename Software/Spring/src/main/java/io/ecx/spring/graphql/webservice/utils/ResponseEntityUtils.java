package io.ecx.spring.graphql.webservice.utils;

import io.ecx.spring.graphql.webservice.models.Korisnik;
import io.ecx.spring.graphql.webservice.models.Mjesto;
import io.ecx.spring.graphql.webservice.models.PotresModel;
import io.ecx.spring.graphql.webservice.models.RestApiOdgovor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class ResponseEntityUtils {

    public static ResponseEntity<RestApiOdgovor<PotresModel>> GetRestApiOdgovorForPotres(final HttpStatus httpStatus, final List<PotresModel> rezultat) {
        final RestApiOdgovor<PotresModel> odgovor = new RestApiOdgovor<>();
        odgovor.setRezultat(rezultat);
        odgovor.setBrojRezultata(odgovor.getRezultat().size());
        odgovor.setStatus(httpStatus);
        return new ResponseEntity<>(odgovor, httpStatus);
    }

    public static ResponseEntity<RestApiOdgovor<PotresModel>> GetRestApiOdgovorForPotres(final HttpStatus httpStatus, final PotresModel model){
        final RestApiOdgovor<PotresModel> odgovor = new RestApiOdgovor<>();
        final List<PotresModel> potres = new ArrayList<>();
        potres.add(model);
        odgovor.setRezultat(potres);
        odgovor.setBrojRezultata(odgovor.getRezultat().size());
        odgovor.setStatus(httpStatus);
        return new ResponseEntity<>(odgovor, httpStatus);
    }

    public static ResponseEntity<RestApiOdgovor<Korisnik>> GetRestApiOdgovorForKorisnik(final HttpStatus httpStatus, final List<Korisnik> rezultat) {
        final RestApiOdgovor<Korisnik> odgovor = new RestApiOdgovor<>();
        odgovor.setRezultat(rezultat);
        odgovor.setBrojRezultata(odgovor.getRezultat().size());
        odgovor.setStatus(httpStatus);
        return new ResponseEntity<>(odgovor, httpStatus);
    }

    public static ResponseEntity<RestApiOdgovor<Korisnik>> GetRestApiOdgovorForKorisnik(final HttpStatus httpStatus, final Korisnik model) {
        final RestApiOdgovor<Korisnik> odgovor = new RestApiOdgovor<>();
        final List<Korisnik> korisnik = new ArrayList<>();
        korisnik.add(model);
        odgovor.setRezultat(korisnik);
        odgovor.setBrojRezultata(odgovor.getRezultat().size());
        odgovor.setStatus(httpStatus);
        return new ResponseEntity<>(odgovor, httpStatus);
    }

    public static ResponseEntity<RestApiOdgovor<Mjesto>> GetRestApiOdgovorForMjesto(final HttpStatus httpStatus, final List<Mjesto> rezultat) {
        final RestApiOdgovor<Mjesto> odgovor = new RestApiOdgovor<>();
        odgovor.setRezultat(rezultat);
        odgovor.setBrojRezultata(odgovor.getRezultat().size());
        odgovor.setStatus(httpStatus);
        return new ResponseEntity<>(odgovor, httpStatus);
    }
    public static ResponseEntity<RestApiOdgovor<Mjesto>> GetRestApiOdgovorForMjesto(final HttpStatus httpStatus, final Mjesto rezultat) {
        final RestApiOdgovor<Mjesto> odgovor = new RestApiOdgovor<>();
        final List<Mjesto> mjesto = new ArrayList<>();
        mjesto.add(rezultat);
        odgovor.setRezultat(mjesto);
        odgovor.setBrojRezultata(odgovor.getRezultat().size());
        odgovor.setStatus(httpStatus);
        return new ResponseEntity<>(odgovor, httpStatus);
    }

}
