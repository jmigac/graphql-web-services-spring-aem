package io.ecx.spring.graphql.webservice.controllers;

import io.ecx.spring.graphql.webservice.models.PotresModel;
import io.ecx.spring.graphql.webservice.services.PotresServis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(path = "/api/v1/potresi")
public class PotresKontroler {

    @Autowired
    private PotresServis potresServis;

    @GetMapping
    public ResponseEntity<List<PotresModel>> dohvatiSve() {
        return new ResponseEntity<>(this.potresServis.dohvatiSve(), HttpStatus.OK);
    }

}
