package io.ecx.spring.graphql.webservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Lokacija implements Serializable {

    private Double geografskaSirina;
    private Double geografskaDuzina;
    private Double dubina;

    @JsonProperty("coordinates")
    private void otpakirajKoordinate(final Double[] svojstva) {
        this.geografskaSirina = Optional.ofNullable(svojstva[1]).orElse(0.00);
        this.geografskaDuzina = Optional.ofNullable(svojstva[0]).orElse(0.00);
        this.dubina = Optional.ofNullable(svojstva[2]).orElse(0.00);
    }
}
