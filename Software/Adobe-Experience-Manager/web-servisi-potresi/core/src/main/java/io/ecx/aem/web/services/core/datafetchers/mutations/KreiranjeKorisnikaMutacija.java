package io.ecx.aem.web.services.core.datafetchers.mutations;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import io.ecx.aem.web.services.core.record.KorisnikZapis;
import io.ecx.aem.web.services.core.services.RepozitorijPotresa;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(service = KreiranjeKorisnikaMutacija.class, immediate = true)
public class KreiranjeKorisnikaMutacija implements DataFetcher<KorisnikZapis> {

    private static final String SERVISNI_KORISNIK = "web-services-system-user";
    private static final String IME = "ime";
    private static final String PREZIME = "prezime";
    private static final String EMAIL = "email";
    private static final String KORISNICKO_IME = "korisnickoIme";
    private static final String LOZINKA = "lozinka";

    @Reference
    private RepozitorijPotresa repozitorijPotresa;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    public KorisnikZapis get(final DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        final String ime = dataFetchingEnvironment.getArgument(IME);
        final String prezime = dataFetchingEnvironment.getArgument(PREZIME);
        final String email = dataFetchingEnvironment.getArgument(EMAIL);
        final String korIme = dataFetchingEnvironment.getArgument(KORISNICKO_IME);
        final String lozinka = dataFetchingEnvironment.getArgument(LOZINKA);
        if (StringUtils.isNoneEmpty(ime, prezime, email)) {
            final KorisnikZapis korisnik = KorisnikZapis
                                             .builder()
                                             .ime(ime)
                                             .prezime(prezime)
                                             .email(email)
                                             .korisnickoIme(korIme)
                                             .lozinka(lozinka)
                                             .build();
            try (final ResourceResolver resourceResolver = this.resourceResolverFactory.getServiceResourceResolver(
              this.dohvatiAutentifikacijskePodatke())) {
                return this.repozitorijPotresa.kreiranjeKorisnika(resourceResolver, korisnik);
            } catch (final Exception e) {
                log.error("Greška prilikom dohvatanja potresa", e);
            }
        }
        return null;
    }

    private Map<String, Object> dohvatiAutentifikacijskePodatke() {
        final Map<String, Object> parametri = new HashMap<>();
        parametri.put(ResourceResolverFactory.SUBSERVICE, SERVISNI_KORISNIK);
        return parametri;
    }

}
