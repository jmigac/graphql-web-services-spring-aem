package io.ecx.spring.graphql.webservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Vrijeme implements Serializable {

    private Double temperatura;
    private Double minimalnaTemperatura;
    private Double maksimalnaTemperatura;
    private Double tlak;
    private int vlaznostZraka;
    private Double brzinaVjetra;
    private int smjerPuhanja;

    @JsonProperty("main")
    private void otpakirajSvojstvaVremena(final Map<String, String> svojstva){
        this.temperatura = Double.parseDouble(Optional.ofNullable(svojstva.get("temp")).orElse(StringUtils.EMPTY));
        this.minimalnaTemperatura = Double.parseDouble(Optional.ofNullable(svojstva.get("temp_min")).orElse(StringUtils.EMPTY));
        this.maksimalnaTemperatura = Double.parseDouble(Optional.ofNullable(svojstva.get("temp_max")).orElse(StringUtils.EMPTY));
        this.tlak = Double.parseDouble(Optional.ofNullable(svojstva.get("pressure")).orElse(StringUtils.EMPTY));
        this.vlaznostZraka = Integer.parseInt(Optional.ofNullable(svojstva.get("humidity")).orElse(StringUtils.EMPTY));
    }

    @JsonProperty("wind")
    private void otpakirajSvojstvaVjetra(final Map<String, String> svojstva){
        this.brzinaVjetra = Double.parseDouble(Optional.ofNullable(svojstva.get("speed")).orElse(StringUtils.EMPTY));
        this.smjerPuhanja = Integer.parseInt(Optional.ofNullable(svojstva.get("deg")).orElse(StringUtils.EMPTY));
    }

}
