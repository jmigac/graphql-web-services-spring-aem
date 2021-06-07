package io.ecx.spring.graphql.webservice.services;

import io.ecx.spring.graphql.webservice.models.DnevnikRada;
import io.ecx.spring.graphql.webservice.repository.DnevnikRadaRepozitorij;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DnevnikRadaServis {

    @Autowired
    private DnevnikRadaRepozitorij repozitorij;

    public void spremi(final DnevnikRada dnevnikRada){
        this.repozitorij.save(dnevnikRada);
    }

    public List<DnevnikRada> dohvatiSve(){
        return this.repozitorij.findAll();
    }
}
