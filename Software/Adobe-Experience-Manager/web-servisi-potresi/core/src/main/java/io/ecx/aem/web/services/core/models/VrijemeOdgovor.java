package io.ecx.aem.web.services.core.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

public class VrijemeOdgovor {

    private static final String PROGNOZA_VREMENA = "list";

    @Getter
    @JsonProperty(PROGNOZA_VREMENA)
    private List<VrijemeModel> prognozaVremena;

    public VrijemeModel dohvatiPrvuPrognozu(){
        return this.prognozaVremena.get(0);
    }

    public boolean imaPrognoza(){
        return this.prognozaVremena != null && !this.prognozaVremena.isEmpty();
    }

}
