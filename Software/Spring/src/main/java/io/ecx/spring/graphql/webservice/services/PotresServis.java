package io.ecx.spring.graphql.webservice.services;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.ecx.spring.graphql.webservice.exceptions.BrisanjeNepostojanogModela;
import io.ecx.spring.graphql.webservice.helpers.DnevnikRadaHelper;
import io.ecx.spring.graphql.webservice.models.DnevnikRada;
import io.ecx.spring.graphql.webservice.models.PotresModel;
import io.ecx.spring.graphql.webservice.repository.PotresRepozitorij;

@Service
public class PotresServis {

    @Autowired
    private PotresRepozitorij repozitorij;

    @Autowired
    private DnevnikRadaServis dnevnikRadaServis;

    public PotresModel spremi(final PotresModel potresModel) {
        this.dnevnikRadaServis.spremi(DnevnikRada.builder().radnja(DnevnikRadaHelper.AKCIJA_UPISA_POTRESA_U_BAZU).vrijemeRadnje(new Date()).build());
        return this.repozitorij.save(potresModel);
    }

    public List<PotresModel> dohvatiSve() {
        this.dnevnikRadaServis.spremi(DnevnikRada.builder().radnja(DnevnikRadaHelper.AKCIJA_DOHVATA_SVIH_POTRESA).vrijemeRadnje(new Date()).build());
        return this.repozitorij.findAll();
    }

    public List<PotresModel> dohvatiSve(final int limit) {
        this.dnevnikRadaServis.spremi(DnevnikRada.builder().radnja(DnevnikRadaHelper.AKCIJA_DOHVATA_S_LIMITOM).vrijemeRadnje(new Date()).build());
        return this.repozitorij.findPotresModelByLimit(limit);
    }

    public boolean postojiLiPotres(final String id) {
        this.dnevnikRadaServis.spremi(
          DnevnikRada.builder().radnja(DnevnikRadaHelper.PROVJERA_POSTOJANOSTI_POTRESA).vrijemeRadnje(new Date()).build());
        return this.repozitorij.existsPotresModelById(id);
    }

    public void unesiPotresSProvjerom(final PotresModel model) {
        if (!this.postojiLiPotres(model.getId())) {
            this.spremi(model);
        }
    }

    public PotresModel dohvatiPotresModelPoIdentifikatoruEventa(final String eventId) {
        this.dnevnikRadaServis.spremi(DnevnikRada.builder().radnja(DnevnikRadaHelper.AKCIJA_DOHVATA_POTRESA_PO_ID).vrijemeRadnje(new Date()).build());
        return this.repozitorij.findPotresModelById(eventId);
    }

    public PotresModel azurirajObjektPotresa(final PotresModel potresModel) {
        this.dnevnikRadaServis.spremi(DnevnikRada.builder().radnja(DnevnikRadaHelper.AKCIJA_AZURIRANJA_POTRESA).vrijemeRadnje(new Date()).build());
        return this.repozitorij.save(potresModel);
    }

    @Transactional
    public void brisanjeObjektaPotresa(final String eventId) throws BrisanjeNepostojanogModela {
        if (this.repozitorij.existsPotresModelById(eventId)) {
            this.dnevnikRadaServis.spremi(DnevnikRada.builder().radnja(DnevnikRadaHelper.AKCIJA_BRISANJA_POTRESA).vrijemeRadnje(new Date()).build());
            this.repozitorij.deletePotresModelById(eventId);
        } else {
            throw new BrisanjeNepostojanogModela("Nije moguće pokrenuti izvršavanje brisanje modela potresa kada ne postoji.");
        }
    }

    public List<PotresModel> dohvatiPotreseStarijeOdVremena(final Long vrijeme) {
        return this.repozitorij.findPotresModelsByVrijemePotresaGreaterThan(vrijeme);
    }

}
