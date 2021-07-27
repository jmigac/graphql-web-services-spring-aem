package io.ecx.aem.web.services.core.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PozicijaOdgovor {

    private static final String PODACI = "data";

    @JsonProperty(PODACI)
    public List<PozicijaModel> pozicija;

}
