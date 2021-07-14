package io.ecx.spring.graphql.webservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PozicijaOdgovor {

    private static final String PODACI = "data";

    @JsonProperty(PODACI)
    public List<Mjesto> mjesto;

}
