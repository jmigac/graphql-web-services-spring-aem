package io.ecx.aem.web.services.core.models;

import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class PotresModel {

    private static final String API_OBJEKT_SVOJSTVA = "properties";
    private static final String API_SVOJSTVO_MJESTO = "place";
    private static final String API_SVOJSTVO_MAGNITUDA = "mag";
    private static final String API_SVOJSTVO_VRIJEME = "time";
    private static final String API_SVOJSTVO_URL = "url";
    private static final String API_SVOJSTVO_TIP_MAGNITUDE = "magType";
    private static final String API_SVOJSTVO_NASLOV = "title";
    private static final String API_SVOJSTVO_LOKACIJA = "lokacija";
    private static final String API_OBJEKT_LOKACIJA = "geometry";
    private static final String OBJEKT_MJESTO = "pozicija";

    @ValueMapValue
    @Default(values = StringUtils.EMPTY)
    private String id;

    @ValueMapValue
    @Default(doubleValues = 0)
    private Double magnituda;

    @ValueMapValue
    @Default(values = StringUtils.EMPTY)
    private String deskriptivniOpisMjesta;

    @ValueMapValue
    @Default(values = StringUtils.EMPTY)
    private Long vrijemePotresa;

    @ValueMapValue
    @Default(values = StringUtils.EMPTY)
    private String url;

    @ValueMapValue
    private boolean tsunami;

    @ValueMapValue
    @Default(values = StringUtils.EMPTY)
    private String naziv;

    @ValueMapValue
    @Default(values = StringUtils.EMPTY)
    private String tipMagnitude;

    @ChildResource
    private VrijemeModel vrijeme;

    @ChildResource
    private LokacijaModel lokacija;

    @ChildResource(name = OBJEKT_MJESTO)
    private PozicijaModel mjesto;

    @JsonIgnore
    private VrijemeModel modelVremena;

    @JsonIgnore
    private PozicijaModel modelPozicije;

    @JsonProperty(API_OBJEKT_SVOJSTVA)
    private void otpakirajSlozeniPaket(final Map<String, String> svojstva) {
        this.deskriptivniOpisMjesta = Optional.ofNullable(svojstva.get(API_SVOJSTVO_MJESTO)).orElse(StringUtils.EMPTY);
        this.magnituda = Double.parseDouble(Optional.ofNullable(svojstva.get(API_SVOJSTVO_MAGNITUDA)).orElse("0"));
        this.vrijemePotresa = Long.parseLong(Optional.ofNullable(svojstva.get(API_SVOJSTVO_VRIJEME)).orElse("0"));
        this.url = Optional.ofNullable(svojstva.get(API_SVOJSTVO_URL)).orElse(StringUtils.EMPTY);
        this.tipMagnitude = Optional.ofNullable(svojstva.get(API_SVOJSTVO_TIP_MAGNITUDE)).orElse(StringUtils.EMPTY);
        this.naziv = Optional.ofNullable(svojstva.get(API_SVOJSTVO_NASLOV)).orElse(StringUtils.EMPTY);
    }

    @JsonProperty(API_SVOJSTVO_LOKACIJA)
    public LokacijaModel getLokacija() {
        return this.lokacija;
    }

    @JsonProperty(API_OBJEKT_LOKACIJA)
    public void setLokacija(final LokacijaModel lokacija) {
        this.lokacija = lokacija;
    }

    @JsonProperty(API_SVOJSTVO_LOKACIJA)
    public void setLokacijuPoslijeAzuriranja(final LokacijaModel lokacija) {
        this.lokacija = lokacija;
    }

}
