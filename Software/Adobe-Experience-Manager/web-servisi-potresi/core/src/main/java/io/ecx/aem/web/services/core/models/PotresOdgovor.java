package io.ecx.aem.web.services.core.models;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PotresOdgovor {

    private static final String SVOJSTVA = "features";

    @Getter
    @JsonProperty(SVOJSTVA)
    private List<PotresModel> potresi;

    public boolean imaPotresa() {
        return Objects.nonNull(this.potresi) && !this.potresi.isEmpty();
    }

}
