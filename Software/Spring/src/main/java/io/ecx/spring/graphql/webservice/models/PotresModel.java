package io.ecx.spring.graphql.webservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = PotresModel.NAZIV_TABLICE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PotresModel {

    public static final String NAZIV_TABLICE = "potres";
    private static final String BINARNI_STUPAC = "blob";
    private static final String API_OBJEKT_SVOJSTVA = "properties";
    private static final String API_SVOJSTVO_MJESTO = "place";
    private static final String API_SVOJSTVO_MAGNITUDA = "mag";
    private static final String API_SVOJSTVO_VRIJEME = "time";
    private static final String API_SVOJSTVO_URL = "url";
    private static final String API_SVOJSTVO_TIP_MAGNITUDE = "magType";
    private static final String API_SVOJSTVO_NASLOV = "title";
    private static final String API_SVOJSTVO_LOKACIJA = "lokacija";
    private static final String API_OBJEKT_LOKACIJA = "geometry";

    @Id
    @GeneratedValue
    private Long idPotresa;
    private String id;
    private Double magnituda;
    private String deskriptivniOpisMjesta;
    private Long vrijemePotresa;
    private String url;
    private boolean tsunami;
    private String naziv;

    @Transient
    @JsonIgnore
    private String tipMagnitudeJson;

    @Column(columnDefinition = BINARNI_STUPAC)
    private Lokacija lokacija;

    @Column(columnDefinition = BINARNI_STUPAC)
    private Vrijeme vrijeme;

    @ManyToOne
    private Mjesto mjesto;

    @ManyToOne
    private TipMagnitude tipMagnitude;

    @JsonProperty(API_OBJEKT_SVOJSTVA)
    private void otpakirajSlozeniJSON(final Map<String, String> svojstva) {
        this.deskriptivniOpisMjesta = Optional.ofNullable(svojstva.get(API_SVOJSTVO_MJESTO)).orElse(StringUtils.EMPTY);
        this.magnituda = Double.parseDouble(Optional.ofNullable(svojstva.get(API_SVOJSTVO_MAGNITUDA)).orElse("0"));
        this.vrijemePotresa = Long.parseLong(Optional.ofNullable(svojstva.get(API_SVOJSTVO_VRIJEME)).orElse("0"));
        this.url = Optional.ofNullable(svojstva.get(API_SVOJSTVO_URL)).orElse(StringUtils.EMPTY);
        this.tipMagnitudeJson = Optional.ofNullable(svojstva.get(API_SVOJSTVO_TIP_MAGNITUDE)).orElse(StringUtils.EMPTY);
        this.naziv = Optional.ofNullable(svojstva.get(API_SVOJSTVO_NASLOV)).orElse(StringUtils.EMPTY);
    }

    @JsonProperty(API_SVOJSTVO_LOKACIJA)
    public Lokacija getLokacija() {
        return this.lokacija;
    }

    @JsonProperty(API_OBJEKT_LOKACIJA)
    public void setLokacija(final Lokacija lokacija) {
        this.lokacija = lokacija;
    }

    @JsonProperty(API_SVOJSTVO_LOKACIJA)
    public void setLokacijuPoslijeAzuriranja(final Lokacija lokacija) {
        this.lokacija = lokacija;
    }

}
