package io.ecx.spring.graphql.webservice.controllers;

import io.ecx.spring.graphql.webservice.exceptions.BrisanjeNepostojanogModela;
import io.ecx.spring.graphql.webservice.models.PotresModel;
import io.ecx.spring.graphql.webservice.models.RestApiOdgovor;
import io.ecx.spring.graphql.webservice.services.PotresServis;
import io.ecx.spring.graphql.webservice.utils.ResponseEntityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Slf4j
@CrossOrigin
@Controller
@RequestMapping(path = "/api/v1/potresi")
public class PotresKontroler {

    @Autowired
    private PotresServis potresServis;

    @GetMapping
    public ResponseEntity<RestApiOdgovor<PotresModel>> dohvatiSvePotrese(@RequestHeader(value = "limit", defaultValue = "-1") final String limitPotresa) {
        try {
            Integer.parseInt(limitPotresa);
        } catch (final NumberFormatException e) {
            return ResponseEntityUtils.GetRestApiOdgovorForPotres(HttpStatus.BAD_REQUEST, new ArrayList<>());
        }
        if (StringUtils.equals("-1", limitPotresa)) {
            return ResponseEntityUtils.GetRestApiOdgovorForPotres(HttpStatus.OK, this.potresServis.dohvatiSve());
        } else {
            return ResponseEntityUtils.GetRestApiOdgovorForPotres(HttpStatus.OK, this.potresServis.dohvatiSve(Integer.parseInt(limitPotresa)));
        }
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<RestApiOdgovor<PotresModel>> dohvatiOdredeniPotres(@PathVariable("eventId") final String eventId) {
        if (StringUtils.isNotEmpty(eventId)) {
            if (this.potresServis.postojiLiPotres(eventId)) {
                return ResponseEntityUtils.GetRestApiOdgovorForPotres(HttpStatus.OK, this.potresServis.dohvatiPotresModelPoIdentifikatoruEventa(eventId));
            } else {
                return ResponseEntityUtils.GetRestApiOdgovorForPotres(HttpStatus.NOT_FOUND, new ArrayList<>());
            }
        } else {
            return ResponseEntityUtils.GetRestApiOdgovorForPotres(HttpStatus.BAD_REQUEST, new ArrayList<>());
        }
    }

    @PutMapping("/azuriraj")
    public ResponseEntity<RestApiOdgovor<PotresModel>> azurirajPotres(@RequestBody final PotresModel model) {
        if (model != null) {
            if (this.potresServis.postojiLiPotres(model.getId())) {
                return ResponseEntityUtils.GetRestApiOdgovorForPotres(HttpStatus.OK, this.potresServis.azurirajObjektPotresa(model));
            } else {
                return ResponseEntityUtils.GetRestApiOdgovorForPotres(HttpStatus.NOT_FOUND, new ArrayList<>());
            }
        } else {
            return ResponseEntityUtils.GetRestApiOdgovorForPotres(HttpStatus.BAD_REQUEST, new ArrayList<>());
        }
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<RestApiOdgovor<PotresModel>> brisanjePotresa(@PathVariable("eventId") final String eventId){
        if(StringUtils.isNotEmpty(eventId)){
            if(this.potresServis.postojiLiPotres(eventId)){
                try {
                    this.potresServis.brisanjeObjektaPotresa(eventId);
                } catch (final BrisanjeNepostojanogModela e) {
                    log.error(e.getMessage(), e);
                }
                return ResponseEntityUtils.GetRestApiOdgovorForPotres(HttpStatus.OK, new ArrayList<>());
            }else{
                return ResponseEntityUtils.GetRestApiOdgovorForPotres(HttpStatus.NOT_FOUND, new ArrayList<>());
            }
        }else{
            return ResponseEntityUtils.GetRestApiOdgovorForPotres(HttpStatus.BAD_REQUEST, new ArrayList<>());
        }
    }

}
