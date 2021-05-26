package io.ecx.spring.graphql.webservice.services;

import io.ecx.spring.graphql.webservice.models.PotresLog;
import io.ecx.spring.graphql.webservice.repository.PotresLogRepozitorij;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PotresLogServis {

    @Autowired
    private PotresLogRepozitorij repositorij;

    public boolean postojiZapisDana(final Date date){
        return this.repositorij.existsPotresLogByDatum(date);
    }

    public void spremi(final PotresLog potresLog){
        this.repositorij.save(potresLog);
    }

}
