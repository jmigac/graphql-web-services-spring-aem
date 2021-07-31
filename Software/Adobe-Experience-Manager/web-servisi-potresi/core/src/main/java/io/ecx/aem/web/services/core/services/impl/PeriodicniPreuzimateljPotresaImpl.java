package io.ecx.aem.web.services.core.services.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.osgi.service.component.annotations.Component;

import io.ecx.aem.web.services.core.models.LokacijaModel;
import io.ecx.aem.web.services.core.models.PotresModel;
import io.ecx.aem.web.services.core.models.PotresOdgovor;
import io.ecx.aem.web.services.core.models.PozicijaModel;
import io.ecx.aem.web.services.core.models.VrijemeModel;
import io.ecx.aem.web.services.core.services.PeriodicniPreuzimateljPotresa;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(service = PeriodicniPreuzimateljPotresa.class, immediate = true)
public class PeriodicniPreuzimateljPotresaImpl implements PeriodicniPreuzimateljPotresa {

    private static final String CONTENT_DIREKTORIJ = "/content";
    private static final String POTRESI_DIREKTORIJ = "potresi";

    @Override
    public Resource dohvatiKorjenskiDirektorij(final ResourceResolver resourceResolver) {
        return resourceResolver.getResource(CONTENT_DIREKTORIJ);
    }

    @Override
    public void spremiSkupPotresa(final List<PotresModel> odgovor, final ResourceResolver resourceResolver, final String datum) {
        try {
            final Resource korjen = resourceResolver.getResource(CONTENT_DIREKTORIJ);
            final Resource potresi = this.dohvatiCvor(Objects.requireNonNull(korjen), POTRESI_DIREKTORIJ, resourceResolver).orElse(null);
            final Resource datumResurs = this.dohvatiCvor(Objects.requireNonNull(potresi), datum, resourceResolver).orElse(null);
            this.spremiPotres(Objects.requireNonNull(datumResurs), odgovor, resourceResolver);
        } catch (final NoSuchElementException | NullPointerException e) {
            log.error("Error while accessing resource which doesn't exist", e);
        }
    }

    public void spremiPotres(final Resource cvor, final List<PotresModel> odgovor, final ResourceResolver resourceResolver) {
        int redniBrCvora = 0;
        if (!cvor.hasChildren()) {
            for (final PotresModel model : odgovor) {
                try {
                    final Resource cvorPotresa = resourceResolver.create(
                      cvor, StringUtils.join("potres_", String.valueOf(redniBrCvora)), this.kreirajSvojstvaPotresa(model));
                    if (model.getModelVremena() != null) {
                        resourceResolver.create(cvorPotresa, "vrijeme", this.kreirajSvojstvaVremena(model.getModelVremena()));
                    }
                    if (model.getModelPozicije() != null) {
                        resourceResolver.create(cvorPotresa, "pozicija", this.kreirajSvojstvaPozicije(model.getModelPozicije()));
                    }
                    if (model.getLokacija() != null) {
                        resourceResolver.create(cvorPotresa, "lokacija", this.kreirajSvojstvaLokacije(model.getLokacija()));
                    }
                    redniBrCvora++;
                    resourceResolver.commit();
                } catch (final PersistenceException | NullPointerException e) {
                    log.error("", e);
                }
            }
        }
    }

    @Override
    public boolean provjeriPostojanostPotresa(final LocalDate datum, final ResourceResolver resourceResolver) {
        return resourceResolver.getResource("/content/potresi/" + datum.toString()) != null;
    }

    private String dohvatiStringVrijednost(final String svojstvo) {
        return StringUtils.isNotEmpty(svojstvo) ? svojstvo : StringUtils.EMPTY;
    }

    private Long dohvatiLongVrijednost(final Long svojstvo) {
        return svojstvo != null ? svojstvo : 0;
    }

    private Double dohvatiDoubleVrijednost(final Double svojstvo) {
        return svojstvo != null ? svojstvo : 0;
    }

    private Map<String, Object> kreirajSvojstvaPotresa(final PotresModel potresModel) {
        final Map<String, Object> properties = new HashMap<>();
        properties.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
        properties.put(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY, "entitet/potres");
        properties.put("id", this.dohvatiStringVrijednost(potresModel.getId()));
        properties.put("magnituda", this.dohvatiDoubleVrijednost(potresModel.getMagnituda()));
        properties.put("deskriptivniOpisMjesta", this.dohvatiStringVrijednost(potresModel.getDeskriptivniOpisMjesta()));
        properties.put("vrijemePotresa", this.dohvatiLongVrijednost(potresModel.getVrijemePotresa()));
        properties.put("url", this.dohvatiStringVrijednost(potresModel.getUrl()));
        properties.put("tsunami", potresModel.isTsunami());
        properties.put("naziv", this.dohvatiStringVrijednost(potresModel.getNaziv()));
        properties.put("tipMagnitude", this.dohvatiStringVrijednost(potresModel.getTipMagnitude()));
        return properties;
    }

    private Map<String, Object> kreirajSvojstvaLokacije(final LokacijaModel model) {
        final Map<String, Object> properties = new HashMap<>();
        properties.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
        properties.put(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY, "entitet/lokacija");
        properties.put("geografskaSirina", this.dohvatiDoubleVrijednost(model.getGeografskaSirina()));
        properties.put("geografskaDuzina", this.dohvatiDoubleVrijednost(model.getGeografskaDuzina()));
        properties.put("dubina", this.dohvatiDoubleVrijednost(model.getDubina()));
        return properties;
    }

    private Map<String, Object> kreirajSvojstvaVremena(final VrijemeModel model) {
        final Map<String, Object> properties = new HashMap<>();
        properties.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
        properties.put(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY, "entitet/vrijeme");
        properties.put("temperatura", this.dohvatiDoubleVrijednost(model.getTemperatura()));
        properties.put("minimalnaTemperatura", this.dohvatiDoubleVrijednost(model.getMinimalnaTemperatura()));
        properties.put("maksimalnaTemperatura", this.dohvatiDoubleVrijednost(model.getMaksimalnaTemperatura()));
        properties.put("tlak", this.dohvatiDoubleVrijednost(model.getTlak()));
        properties.put("brzinaVjetra", this.dohvatiDoubleVrijednost(model.getBrzinaVjetra()));
        properties.put("vlaznostZraka", model.getVlaznostZraka());
        properties.put("smjerPuhanja", model.getSmjerPuhanja());
        return properties;
    }

    private Map<String, Object> kreirajSvojstvaPozicije(final PozicijaModel model) {
        final Map<String, Object> properties = new HashMap<>();
        properties.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
        properties.put(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY, "entitet/pozicija");
        properties.put("naziv", this.dohvatiStringVrijednost(model.getNaziv()));
        properties.put("drzava", this.dohvatiStringVrijednost(model.getDrzava()));
        properties.put("isoKod", this.dohvatiStringVrijednost(model.getIsoKod()));
        properties.put("kontinent", this.dohvatiStringVrijednost(model.getKontinent()));
        properties.put("regija", this.dohvatiStringVrijednost(model.getRegija()));
        return properties;
    }

    private Map<String, Object> kreirajOsnovnaSvojstvaCvora() {
        final Map<String, Object> properties = new HashMap<>();
        properties.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
        return properties;
    }

    private Optional<Resource> dohvatiCvor(final Resource korjenskiCvor, final String cvorDijeteta, final ResourceResolver resourceResolver) {
        Resource resursDijeteta = korjenskiCvor.getChild(cvorDijeteta);
        if (resursDijeteta == null) {
            try {
                resourceResolver.create(korjenskiCvor, cvorDijeteta, this.kreirajOsnovnaSvojstvaCvora());
                resourceResolver.commit();
                resursDijeteta = korjenskiCvor.getChild(cvorDijeteta);
            } catch (final PersistenceException e) {
                log.error("Error while getting or creating child node", e);
            }
        }
        return Optional.ofNullable(resursDijeteta);
    }

}