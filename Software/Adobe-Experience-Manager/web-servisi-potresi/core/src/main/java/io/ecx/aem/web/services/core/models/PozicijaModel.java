package io.ecx.aem.web.services.core.models;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PozicijaModel {

    private static final String NAZIV_MJESTA = "name";
    private static final String DRZAVA = "country";
    private static final String ISO_KOD = "country_code";
    private static final String KONTINENT = "continent";
    private static final String REGIJA = "region";
    private static final String NE_DEFINIRANO = "Nije definirano";

    @ValueMapValue
    @Default(values = StringUtils.EMPTY)
    private String naziv;

    @ValueMapValue
    @Default(values = StringUtils.EMPTY)
    private String drzava;

    @ValueMapValue
    @Default(values = StringUtils.EMPTY)
    private String isoKod;

    @ValueMapValue
    @Default(values = StringUtils.EMPTY)
    private String kontinent;

    @ValueMapValue
    @Default(values = StringUtils.EMPTY)
    private String regija;

    @Override
    public String toString() {
        return this.drzava;
    }

    @JsonProperty(NAZIV_MJESTA)
    public void setNaziv(final String naziv) {
        this.naziv = StringUtils.isNotEmpty(naziv) ? naziv : NE_DEFINIRANO;
    }

    @JsonProperty(DRZAVA)
    public void setDrzava(final String drzava) {
        this.drzava = StringUtils.isNotEmpty(drzava) ? drzava : NE_DEFINIRANO;
    }

    @JsonProperty(ISO_KOD)
    public void setIsoKod(final String isoKod) {
        this.isoKod = StringUtils.isNotEmpty(isoKod) ? isoKod : NE_DEFINIRANO;
    }

    @JsonProperty(KONTINENT)
    public void setKontinent(final String kontinent) {
        this.kontinent = StringUtils.isNotEmpty(kontinent) ? kontinent : NE_DEFINIRANO;
    }

    @JsonProperty(REGIJA)
    public void setRegion(final String regija) {
        this.regija = StringUtils.isNotEmpty(regija) ? regija : NE_DEFINIRANO;
    }

}
