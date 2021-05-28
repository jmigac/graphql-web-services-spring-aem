package io.ecx.spring.graphql.webservice.services;

import io.ecx.spring.graphql.webservice.models.PotresLog;
import io.ecx.spring.graphql.webservice.models.PotresModel;
import io.ecx.spring.graphql.webservice.models.PotresOdgovor;
import io.ecx.spring.graphql.webservice.models.VrijemeOdgovor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

@Component
@Slf4j
public class PotresAPI {

    @Autowired
    private PotresServis potresServis;

    @Autowired
    private PotresLogServis potresLogServis;

    private static final long JEDNODNEVNA_RAZLIKA = 1;
    private final static LocalDate POCETNI_DATUM = LocalDate.now();
    private final static LocalDate KRAJNJI_DATUM = POCETNI_DATUM.plusDays(JEDNODNEVNA_RAZLIKA);
    private static final String EXTERNAL_API_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=%s&endtime=%s";
    private static final String OPEN_WEATHER_MAP = "http://api.openweathermap.org/data/2.5/find?lat=%s&lon=%s&cnt=1&appid=03205f4f54bbc93c4df6a7fe6a0dbb73";

    @Scheduled(cron = "0 0 12 * * ?")
    public void dohvatiPodatke() {
        final RestTemplate restTemplate = new RestTemplate();
        final PotresOdgovor potresOdgovor = restTemplate.getForObject(String.format(EXTERNAL_API_URL, POCETNI_DATUM, KRAJNJI_DATUM), PotresOdgovor.class);
        if (potresOdgovor != null && potresOdgovor.imaPotresov() && this.nijeObradenSkup()) {
            for (final PotresModel model : potresOdgovor.getPotresi()) {
                final RestTemplate restPredlozakZaVrijeme = new RestTemplate();
                try {
                    final VrijemeOdgovor vrijemeOdgovor = restPredlozakZaVrijeme.getForObject(
                            String.format(OPEN_WEATHER_MAP,
                                    model.getLokacija().getGeografskaSirina(),
                                    model.getLokacija().getGeografskaDuzina()),
                            VrijemeOdgovor.class);
                    if (vrijemeOdgovor != null && vrijemeOdgovor.imaPrognoza()) {
                        model.setVrijeme(vrijemeOdgovor.dohvatiPrvuPrognozu());
                    }
                } catch (final Exception e) {
                    log.error("Greška prilikom poziva web servisa za dohvat vremena", e);
                }
                this.potresServis.unesiPotresSProvjerom(model);
            }
            this.zapisiObradenoVrijeme();
        }
    }

    private boolean nijeObradenSkup() {
        return !this.potresLogServis.postojiZapisDana(Date.from(POCETNI_DATUM.atStartOfDay().toInstant(ZoneOffset.UTC)));
    }

    private void zapisiObradenoVrijeme() {
        final PotresLog log = new PotresLog();
        log.setDatum(Date.from(POCETNI_DATUM.atStartOfDay().toInstant(ZoneOffset.UTC)));
        this.potresLogServis.spremi(log);
    }

}
