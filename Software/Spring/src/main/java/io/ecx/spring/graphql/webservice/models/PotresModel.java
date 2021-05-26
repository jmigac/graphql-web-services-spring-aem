package io.ecx.spring.graphql.webservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "potres")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PotresModel {

    @Id
    @GeneratedValue
    private Long idPotresa;
    private String id;
    private Double magnituda;
    private String desktiptivniOpisMjesta;
    private Long vrijemePotresa;
    private String url;
    private boolean tsunami;
    private String tipMagnitude;
    private String naziv;

    @Column(columnDefinition = "blob")
    private Lokacija lokacija;

    @Column(columnDefinition = "blob")
    private Vrijeme vrijeme;

    @JsonProperty("properties")
    private void otpakirajSlozeniJSON(final Map<String, String> svojstva) {
        this.desktiptivniOpisMjesta = Optional.ofNullable(svojstva.get("place")).orElse(StringUtils.EMPTY);
        this.magnituda = Double.parseDouble(Optional.ofNullable(svojstva.get("mag")).orElse(StringUtils.EMPTY));
        this.vrijemePotresa = Long.parseLong(Optional.ofNullable(svojstva.get("time")).orElse(StringUtils.EMPTY));
        this.url = Optional.ofNullable(svojstva.get("url")).orElse(StringUtils.EMPTY);
        this.tipMagnitude = Optional.ofNullable(svojstva.get("magType")).orElse(StringUtils.EMPTY);
        this.naziv = Optional.ofNullable(svojstva.get("title")).orElse(StringUtils.EMPTY);
    }

    @JsonProperty("lokacija")
    public Lokacija getLokacija() {
        return this.lokacija;
    }

    @JsonProperty("geometry")
    public void setLokacija(final Lokacija lokacija) {
        this.lokacija = lokacija;
    }
}
