package io.ecx.spring.graphql.webservice.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity
public class Mjesto {

    private static final String NAZIV_MJESTA = "name";
    private static final String DRZAVA = "country";
    private static final String ISO_KOD = "country_code";
    private static final String KONTINENT = "continent";
    private static final String REGIJA = "region";
    private static final String NE_DEFINIRANO = "Nije definirano";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String naziv;
    private String drzava;
    private String isoKod;
    private String kontinent;
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
