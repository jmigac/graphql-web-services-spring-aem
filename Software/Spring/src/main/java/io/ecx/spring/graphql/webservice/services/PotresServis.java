package io.ecx.spring.graphql.webservice.services;

import io.ecx.spring.graphql.webservice.exceptions.BrisanjeNepostojanogModela;
import io.ecx.spring.graphql.webservice.models.PotresModel;
import io.ecx.spring.graphql.webservice.repository.PotresRepozitorij;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PotresServis {

    @Autowired
    PotresRepozitorij repozitorij;

    public void spremi(final PotresModel potresModel){
        this.repozitorij.save(potresModel);
    }

    public List<PotresModel> dohvatiSve(){
        return this.repozitorij.findAll();
    }

    public List<PotresModel> dohvatiSve(final int limit){
        return this.repozitorij.findPotresModelByLimit(limit);
    }

    public boolean postojiLiPotres(final String id){
        return this.repozitorij.existsPotresModelById(id);
    }

    public void unesiPotresSProvjerom(final PotresModel model){
        if(!this.postojiLiPotres(model.getId())){
            this.spremi(model);
        }
    }

    public PotresModel dohvatiPotresModelPoIdentifikatoruEventa(final String eventId){
        return this.repozitorij.findPotresModelById(eventId);
    }

    public PotresModel azurirajObjektPotresa(final PotresModel potresModel){
        return this.repozitorij.save(potresModel);
    }

    @Transactional
    public void brisanjeObjektaPotresa(final String eventId) throws BrisanjeNepostojanogModela{
        if(this.repozitorij.existsPotresModelById(eventId)){
            this.repozitorij.deletePotresModelById(eventId);
        }else{
            throw new BrisanjeNepostojanogModela("Nije moguće pokrenuti izvršavanje brisanje modela potresa kada ne postoji.");
        }
    }
}
