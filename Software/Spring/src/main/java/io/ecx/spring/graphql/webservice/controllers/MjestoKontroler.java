package io.ecx.spring.graphql.webservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.ecx.spring.graphql.webservice.models.Mjesto;
import io.ecx.spring.graphql.webservice.models.RestApiOdgovor;
import io.ecx.spring.graphql.webservice.services.MjestoServis;
import io.ecx.spring.graphql.webservice.utils.ResponseEntityUtils;

@Controller
@RequestMapping(path = "/api/v1/mjesto")
@CrossOrigin
public class MjestoKontroler {

    @Autowired
    private MjestoServis servis;

    @GetMapping
    public ResponseEntity<RestApiOdgovor<Mjesto>> dohvatiSve() {
        return ResponseEntityUtils.GetRestApiOdgovorForMjesto(HttpStatus.OK, this.servis.dohvatiSve());
    }

    @GetMapping("/{iso}")
    public ResponseEntity<RestApiOdgovor<Mjesto>> dohvati(@PathVariable("iso") final String iso) {
        return ResponseEntityUtils.GetRestApiOdgovorForMjesto(HttpStatus.OK, this.servis.dohvati(iso));
    }

}
