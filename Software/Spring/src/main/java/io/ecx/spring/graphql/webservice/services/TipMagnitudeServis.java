package io.ecx.spring.graphql.webservice.services;

import io.ecx.spring.graphql.webservice.helpers.TipMagnitudeHelper;
import io.ecx.spring.graphql.webservice.models.TipMagnitude;
import io.ecx.spring.graphql.webservice.repository.TipMagnitudeRepozitorij;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipMagnitudeServis {

    @Autowired
    private TipMagnitudeRepozitorij repozitorij;

    public void hardkodirajTipove() {
        if (!this.postojeEntiteti()) {
            TipMagnitudeHelper.dohvatiSveTipoveMagnitude().forEach(this::spremi);
        }
    }

    private boolean postojeEntiteti() {
        return !this.repozitorij.findAll().isEmpty();
    }

    public void spremi(final TipMagnitude tip) {
        this.repozitorij.save(tip);
    }

    public TipMagnitude dohvati(final String tip){
        return this.repozitorij.findTipMagnitudeByTip(tip);
    }

}
