package io.ecx.spring.graphql.webservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Vrijeme implements Serializable {

    private static final String VJETAR = "wind";
    private static final String VRIJEME = "main";
    private static final String TEMPERATURA = "temp";
    private static final String MINIMALNA_TEMPERATURA = "temp_min";
    private static final String MAKSIMALNA_TEMPERATURA = "temp_max";
    private static final String TLAK = "pressure";
    private static final String VLAZNOST = "humidity";
    private static final String BRZINA = "speed";
    private static final String SMJER_VJETRA = "deg";

    private Double temperatura;
    private Double minimalnaTemperatura;
    private Double maksimalnaTemperatura;
    private Double tlak;
    private int vlaznostZraka;
    private Double brzinaVjetra;
    private int smjerPuhanja;

    @JsonProperty(VRIJEME)
    private void otpakirajSvojstvaVremena(final Map<String, String> svojstva) {
        this.temperatura = Double.parseDouble(Optional.ofNullable(svojstva.get(TEMPERATURA)).orElse(StringUtils.EMPTY));
        this.minimalnaTemperatura = Double.parseDouble(Optional.ofNullable(svojstva.get(MINIMALNA_TEMPERATURA)).orElse(StringUtils.EMPTY));
        this.maksimalnaTemperatura = Double.parseDouble(Optional.ofNullable(svojstva.get(MAKSIMALNA_TEMPERATURA)).orElse(StringUtils.EMPTY));
        this.tlak = Double.parseDouble(Optional.ofNullable(svojstva.get(TLAK)).orElse(StringUtils.EMPTY));
        this.vlaznostZraka = Integer.parseInt(Optional.ofNullable(svojstva.get(VLAZNOST)).orElse(StringUtils.EMPTY));
    }

    @JsonProperty(VJETAR)
    private void otpakirajSvojstvaVjetra(final Map<String, String> svojstva) {
        this.brzinaVjetra = Double.parseDouble(Optional.ofNullable(svojstva.get(BRZINA)).orElse(StringUtils.EMPTY));
        this.smjerPuhanja = Integer.parseInt(Optional.ofNullable(svojstva.get(SMJER_VJETRA)).orElse(StringUtils.EMPTY));
    }

    @Override
    public String toString() {
        return this.temperatura + " c";
    }

    public Double getTemperatura() {
        return this.temperatura - 273.15;
    }

}
