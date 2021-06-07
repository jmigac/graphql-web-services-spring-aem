package io.ecx.spring.graphql.webservice.services;

import io.ecx.spring.graphql.webservice.helpers.DnevnikRadaHelper;
import io.ecx.spring.graphql.webservice.models.*;
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

    @Autowired
    private TipMagnitudeServis magnitudeServis;

    @Autowired
    private DnevnikRadaServis dnevnikRadaServis;

    @Autowired
    private MjestoServis mjestoServis;

    private static final long JEDNODNEVNA_RAZLIKA = 1;
    private final static LocalDate POCETNI_DATUM = LocalDate.now();
    private final static LocalDate KRAJNJI_DATUM = POCETNI_DATUM.plusDays(JEDNODNEVNA_RAZLIKA);
    private static final String EXTERNAL_API_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=%s&endtime=%s";
    private static final String OPEN_WEATHER_MAP = "http://api.openweathermap.org/data/2.5/find?lat=%s&lon=%s&cnt=1&appid=03205f4f54bbc93c4df6a7fe6a0dbb73";
    private static final String API_POZICIJA_BASE = "http://api.positionstack.com/v1/reverse?access_key=aedebcda71640db22c1227f233bbe39c&query=%s,%s";

    @Scheduled(cron = "0 */2 * ? * *")
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
                    final RestTemplate predlozakPozicije = new RestTemplate();
                    final String pozicijaAPI = String.format(API_POZICIJA_BASE, model.getLokacija().getGeografskaSirina(), model.getLokacija().getGeografskaDuzina());
                    final PozicijaOdgovor pozicijaOdgovor = predlozakPozicije.getForObject(pozicijaAPI, PozicijaOdgovor.class);
                    final Mjesto dohvacenoMjesto = pozicijaOdgovor.getMjesto().get(0);
                    final Mjesto mjestoBaza = this.mjestoServis.getMjestoPoIsoRegiji(dohvacenoMjesto.getIsoKod(), dohvacenoMjesto.getRegija());
                    if(mjestoBaza==null){
                        model.setMjesto(this.mjestoServis.spremi(dohvacenoMjesto));
                    }else{
                        model.setMjesto(mjestoBaza);
                    }
                } catch (final Exception e) {
                    log.error("Greška prilikom poziva web servisa za dohvat vremena", e);
                }
                model.setTipMagnitude(this.magnitudeServis.dohvati(model.getTipMagnitudeJson()));
                this.potresServis.unesiPotresSProvjerom(model);
                this.dnevnikRadaServis.spremi(DnevnikRada.builder().radnja(DnevnikRadaHelper.AKCIJA_UPISA_POTRESA_U_BAZU).vrijemeRadnje(new Date()).build());
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
