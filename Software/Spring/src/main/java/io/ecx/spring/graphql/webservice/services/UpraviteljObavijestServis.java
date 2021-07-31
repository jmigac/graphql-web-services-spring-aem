package io.ecx.spring.graphql.webservice.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import io.ecx.spring.graphql.webservice.models.KorisnickaObavijest;
import io.ecx.spring.graphql.webservice.models.Korisnik;
import io.ecx.spring.graphql.webservice.models.PotresModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UpraviteljObavijestServis {

    private static final String BAZNI_PREDMET_EMAILA = "Potres u Vašoj državi [%s]";
    private static final String BAZNA_PORUKA_EMAILA = "Dogodio se potres %s \nViše informacija možete pronaći na %s \nDodatne informacije:\n U posljednja 24 sata dogodila se %s potresa.";

    @Autowired
    private KorisnikServis korisnikServis;

    @Autowired
    private KorisnickaObavijestServis korisnickaObavijestServis;

    @Autowired
    private MailServis mailServis;

    public void posaljiObavijesti(final PotresModel potres) {
        final Long pocetakDana = LocalDate.now().atStartOfDay().toEpochSecond(ZoneOffset.UTC);
        final Long krajDana = LocalDate.now().toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.UTC);
        final List<Korisnik> korisnici = this.korisnikServis.dohvatiSve();
        for (final Korisnik korisnik : korisnici) {
            if (!this.korisnickaObavijestServis.postojanostObavijesti(korisnik, potres)){
                final KorisnickaObavijest novaObavijest = KorisnickaObavijest
                                                            .builder()
                                                            .korisnik(korisnik)
                                                            .vrijemeObavijesti(new Date())
                                                            .potres(potres)
                                                            .build();
                this.korisnickaObavijestServis.spremi(novaObavijest);
                final SimpleMailMessage poruka = new SimpleMailMessage();
                poruka.setFrom("potresi@juricamigac.com");
                poruka.setTo(korisnik.getEmail());
                poruka.setSubject(String.format(BAZNI_PREDMET_EMAILA, potres.getMjesto().getIsoKod()));
                poruka.setText(StringUtils.join("Dogodio se potres ", potres.getNaziv(), System.lineSeparator(), "Više informacija možeš pronaći na ", potres.getUrl()));
                this.mailServis.send(poruka);
            }
        }
    }

}
