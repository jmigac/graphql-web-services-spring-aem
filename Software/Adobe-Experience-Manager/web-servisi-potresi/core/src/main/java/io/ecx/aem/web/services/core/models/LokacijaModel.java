package io.ecx.aem.web.services.core.models;

import java.util.Optional;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LokacijaModel {

    private static final String KOORDINATE = "coordinates";

    @ValueMapValue
    @Default(doubleValues = 0)
    private Double geografskaSirina;

    @ValueMapValue
    @Default(doubleValues = 0)
    private Double geografskaDuzina;

    @ValueMapValue
    @Default(doubleValues = 0)
    private Double dubina;

    @JsonProperty(KOORDINATE)
    private void otpakirajKoordinate(final Double[] svojstva) {
        this.geografskaSirina = Optional.ofNullable(svojstva[1]).orElse(0.00);
        this.geografskaDuzina = Optional.ofNullable(svojstva[0]).orElse(0.00);
        this.dubina = Optional.ofNullable(svojstva[2]).orElse(0.00);
    }

    @Override
    public String toString() {
        return this.geografskaSirina + ", " + this.geografskaDuzina;
    }

}
