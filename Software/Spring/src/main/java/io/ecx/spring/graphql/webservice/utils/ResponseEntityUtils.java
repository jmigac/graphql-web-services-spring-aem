package io.ecx.spring.graphql.webservice.utils;

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

}
