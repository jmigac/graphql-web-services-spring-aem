package io.ecx.aem.web.services.core.jobs;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.ecx.aem.web.services.core.models.PotresModel;
import io.ecx.aem.web.services.core.models.PotresOdgovor;
import io.ecx.aem.web.services.core.models.PozicijaModel;
import io.ecx.aem.web.services.core.models.PozicijaOdgovor;
import io.ecx.aem.web.services.core.models.VrijemeOdgovor;
import io.ecx.aem.web.services.core.services.PeriodicniPreuzimateljPotresa;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(service = JobConsumer.class, immediate = true, property = {
  JobConsumer.PROPERTY_TOPICS + "=" + PeriodicniPreuzimateljPotresaJob.JOB_TOPIC })
public class PeriodicniPreuzimateljPotresaJob implements JobConsumer {

    static final String JOB_TOPIC = "PeriodicniPreuzimatelj";
    private static final String SERVISNI_KORISNIK = "web-services-system-user";
    private static final long JEDNODNEVNA_RAZLIKA = 1;
    private static final LocalDate POCETNI_DATUM = LocalDate.now();
    private static final LocalDate KRAJNJI_DATUM = POCETNI_DATUM.plusDays(JEDNODNEVNA_RAZLIKA);
    private static final String EXTERNAL_API_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=%s&endtime=%s";
    private static final String OPEN_WEATHER_MAP = "http://api.openweathermap.org/data/2.5/find?lat=%s&lon=%s&cnt=1&appid=03205f4f54bbc93c4df6a7fe6a0dbb73";
    private static final String API_POZICIJA_BASE = "http://api.positionstack.com/v1/reverse?access_key=aedebcda71640db22c1227f233bbe39c&query=%s,%s";

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private PeriodicniPreuzimateljPotresa periodicniPreuzimateljPotresa;

    @Override
    public JobResult process(final Job job) {
        try (
          final ResourceResolver resourceResolver = this.resourceResolverFactory.getServiceResourceResolver(this.dohvatiAutentifikacijskePodatke())) {
            final List<PotresModel> noviPotresi = new ArrayList<>();
            if (!this.postojiRoditeljskiCvor(resourceResolver)) {
                try {
                    final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    final URL url = new URL(String.format(EXTERNAL_API_URL, POCETNI_DATUM, KRAJNJI_DATUM));
                    final PotresOdgovor odgovor = mapper.readValue(url, PotresOdgovor.class);
                    if (odgovor.imaPotresa()) {
                        for (final PotresModel model : odgovor.getPotresi()) {
                            try {
                                final ObjectMapper vrijemeMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                                final URL urlVrijeme = new URL(String.format(OPEN_WEATHER_MAP, model.getLokacija().getGeografskaSirina(), model.getLokacija().getGeografskaDuzina()));
                                final VrijemeOdgovor vrijemeOdgovor = vrijemeMapper.readValue(urlVrijeme, VrijemeOdgovor.class);
                                if (vrijemeOdgovor != null && vrijemeOdgovor.imaPrognoza()) {
                                    model.setModelVremena(vrijemeOdgovor.dohvatiPrvuPrognozu());
                                }
                                final ObjectMapper lokacijaMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                                final URL urlLokacija = new URL(String.format(API_POZICIJA_BASE, model.getLokacija().getGeografskaSirina(), model.getLokacija().getGeografskaDuzina()));
                                final PozicijaOdgovor pozicijaOdgovor = lokacijaMapper.readValue(urlLokacija, PozicijaOdgovor.class);
                                final PozicijaModel dohvacenoMjesto = pozicijaOdgovor.pozicija.get(0);
                                if (dohvacenoMjesto != null) {
                                    model.setModelPozicije(dohvacenoMjesto);
                                }
                                noviPotresi.add(model);
                            } catch (final Exception e) {
                                log.error("Error during fetching data from external services");
                            }
                        }
                        this.periodicniPreuzimateljPotresa.spremiSkupPotresa(noviPotresi, resourceResolver, POCETNI_DATUM.toString());
                    }
                    return JobResult.OK;
                } catch (final RuntimeException | MalformedURLException e) {
                    log.error("Greška tijekom autentifikacije sistemskog korisnika.", e);
                    return JobResult.FAILED;
                } catch (final JsonMappingException e) {
                    log.error("Greška prilikom mapiranja JSON-a na model", e);
                    return JobResult.FAILED;
                } catch (final JsonParseException e) {
                    log.error("Greška prilikom parsiranja JSON-a", e);
                    return JobResult.FAILED;
                } catch (final IOException e) {
                    log.error("Greška prilikom I/O", e);
                    return JobResult.FAILED;
                }
            } else {
                return JobResult.FAILED;
            }
        } catch (final RuntimeException | LoginException e) {
            log.error("Exception in during authentification of system user", e);
            return JobResult.FAILED;
        }
    }

    private Map<String, Object> dohvatiAutentifikacijskePodatke() {
        final Map<String, Object> parametri = new HashMap<>();
        parametri.put(ResourceResolverFactory.SUBSERVICE, SERVISNI_KORISNIK);
        return parametri;
    }

    private boolean postojiRoditeljskiCvor(final ResourceResolver resourceResolver) {
        return resourceResolver.getResource("/content/potresi/" + POCETNI_DATUM.toString()) != null;
    }

}