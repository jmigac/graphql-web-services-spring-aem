package io.ecx.spring.graphql.webservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class PotresOdgovor {

    private static final String SVOJSTVA = "features";

    @Getter
    @JsonProperty(SVOJSTVA)
    private List<PotresModel> potresi;

    public boolean imaPotresov(){
        return this.potresi != null && !this.potresi.isEmpty();
    }

}
