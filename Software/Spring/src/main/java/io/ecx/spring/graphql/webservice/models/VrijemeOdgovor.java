package io.ecx.spring.graphql.webservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

public class VrijemeOdgovor {

    private static final String PROGNOZA_VREMENA = "list";

    @Getter
    @JsonProperty(PROGNOZA_VREMENA)
    List<Vrijeme> prognozaVremena;

    public Vrijeme dohvatiPrvuPrognozu(){
        return this.prognozaVremena.get(0);
    }

    public boolean imaPrognoza(){
        return this.prognozaVremena != null && !this.prognozaVremena.isEmpty();
    }
}
