package io.ecx.spring.graphql.webservice.services;

import io.ecx.spring.graphql.webservice.models.PotresModel;
import io.ecx.spring.graphql.webservice.repository.PotresRepozitorij;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public boolean postojiLiPotres(final String id){
        return this.repozitorij.existsPotresModelById(id);
    }

    public void unesiPotresSProvjerom(final PotresModel model){
        if(!this.postojiLiPotres(model.getId())){
            this.spremi(model);
        }
    }
}
