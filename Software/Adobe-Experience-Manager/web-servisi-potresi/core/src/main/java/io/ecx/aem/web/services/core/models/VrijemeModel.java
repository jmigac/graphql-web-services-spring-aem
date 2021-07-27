package io.ecx.aem.web.services.core.models;

import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
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
public class VrijemeModel {

    private static final String VJETAR = "wind";
    private static final String VRIJEME = "main";
    private static final String TEMPERATURA = "temp";
    private static final String MINIMALNA_TEMPERATURA = "temp_min";
    private static final String MAKSIMALNA_TEMPERATURA = "temp_max";
    private static final String TLAK = "pressure";
    private static final String VLAZNOST = "humidity";
    private static final String BRZINA = "speed";
    private static final String SMJER_VJETRA = "deg";
    private static final Double KELVIN = 273.15;

    @ValueMapValue
    @Default(doubleValues = 0)
    private Double temperatura;

    @ValueMapValue
    @Default(doubleValues = 0)
    private Double minimalnaTemperatura;

    @ValueMapValue
    @Default(doubleValues = 0)
    private Double maksimalnaTemperatura;

    @ValueMapValue
    @Default(doubleValues = 0)
    private Double tlak;

    @ValueMapValue
    @Default(doubleValues = 0)
    private Double brzinaVjetra;

    @ValueMapValue
    private int vlaznostZraka;

    @ValueMapValue
    private int smjerPuhanja;

    @JsonProperty(VRIJEME)
    private void otpakirajSvojstvaVremena(final Map<String, String> svojstva) {
        this.temperatura = Double.parseDouble(Optional.ofNullable(svojstva.get(TEMPERATURA)).orElse("0"));
        this.minimalnaTemperatura = Double.parseDouble(Optional.ofNullable(svojstva.get(MINIMALNA_TEMPERATURA)).orElse("0"));
        this.maksimalnaTemperatura = Double.parseDouble(Optional.ofNullable(svojstva.get(MAKSIMALNA_TEMPERATURA)).orElse("0"));
        this.tlak = Double.parseDouble(Optional.ofNullable(svojstva.get(TLAK)).orElse(StringUtils.EMPTY));
        this.vlaznostZraka = Integer.parseInt(Optional.ofNullable(svojstva.get(VLAZNOST)).orElse("0"));
    }

    @JsonProperty(VJETAR)
    private void otpakirajSvojstvaVjetra(final Map<String, String> svojstva) {
        this.brzinaVjetra = Double.parseDouble(Optional.ofNullable(svojstva.get(BRZINA)).orElse("0"));
        this.smjerPuhanja = Integer.parseInt(Optional.ofNullable(svojstva.get(SMJER_VJETRA)).orElse("0"));
    }

    @Override
    public String toString() {
        return this.getTemperatura() + " c";
    }

    public Double getTemperatura() {
        return this.temperatura - KELVIN;
    }

    public Double getMinimalnaTemperatura() {
        return this.minimalnaTemperatura - KELVIN;
    }

    public Double getMaksimalnaTemperatura() {
        return this.maksimalnaTemperatura - KELVIN;
    }

}
