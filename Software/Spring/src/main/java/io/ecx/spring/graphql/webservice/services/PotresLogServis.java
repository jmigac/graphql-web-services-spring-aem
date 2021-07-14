package io.ecx.spring.graphql.webservice.services;

import io.ecx.spring.graphql.webservice.helpers.DnevnikRadaHelper;
import io.ecx.spring.graphql.webservice.models.DnevnikRada;
import io.ecx.spring.graphql.webservice.models.PotresLog;
import io.ecx.spring.graphql.webservice.repository.DnevnikRadaRepozitorij;
import io.ecx.spring.graphql.webservice.repository.PotresLogRepozitorij;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PotresLogServis {

    @Autowired
    private PotresLogRepozitorij repositorij;

    @Autowired
    private DnevnikRadaServis dnevnikRadaServis;

    public boolean postojiZapisDana(final Date date){
        this.dnevnikRadaServis.spremi(DnevnikRada
                .builder()
                .radnja(DnevnikRadaHelper.PROVJERA_POSTOJANOSTI_ZAPISA_LOG)
                .vrijemeRadnje(new Date())
                .build());
        return this.repositorij.existsPotresLogByDatum(date);
    }

    public void spremi(final PotresLog potresLog){
        this.dnevnikRadaServis.spremi(DnevnikRada
                .builder()
                .radnja(DnevnikRadaHelper.AKCIJA_ZAPISA_LOGA)
                .vrijemeRadnje(new Date())
                .build());
        this.repositorij.save(potresLog);
    }

}
