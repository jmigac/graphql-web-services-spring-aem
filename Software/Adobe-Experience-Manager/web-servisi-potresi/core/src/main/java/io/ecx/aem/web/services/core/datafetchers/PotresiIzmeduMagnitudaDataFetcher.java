package io.ecx.aem.web.services.core.datafetchers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.day.cq.search.QueryBuilder;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import io.ecx.aem.web.services.core.models.PotresModel;
import io.ecx.aem.web.services.core.services.RepozitorijPotresa;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(service = PotresiIzmeduMagnitudaDataFetcher.class, immediate = true)
public class PotresiIzmeduMagnitudaDataFetcher implements DataFetcher<List<PotresModel>> {

    private static final String SERVISNI_KORISNIK = "web-services-system-user";
    private static final String MINIMALNA_MAGNITUDA = "minMagnituda";
    private static final String MAKSIMALNA_MAGNITUDA = "maxMagnituda";

    @Reference
    private QueryBuilder queryBuilder;

    @Reference
    private RepozitorijPotresa repozitorijPotresa;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    public List<PotresModel> get(final DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        List<PotresModel> potresi = new ArrayList<>();
        final Double minMagnituda = dataFetchingEnvironment.getArgument(MINIMALNA_MAGNITUDA);
        final Double maxMagnituda = dataFetchingEnvironment.getArgument(MAKSIMALNA_MAGNITUDA);
        if (minMagnituda != null && maxMagnituda != null) {
            try (final ResourceResolver resourceResolver = this.resourceResolverFactory.getServiceResourceResolver(this.dohvatiAutentifikacijskePodatke())) {
                potresi = this.repozitorijPotresa.dohvatiSvePotreseUnutarMagnitude(resourceResolver, minMagnituda, maxMagnituda);
            } catch (final Exception e) {
                log.error("Greška prilikom dohvatanja potresa", e);
            }
        }
        return potresi;
    }

    private Map<String, Object> dohvatiAutentifikacijskePodatke() {
        final Map<String, Object> parametri = new HashMap<>();
        parametri.put(ResourceResolverFactory.SUBSERVICE, SERVISNI_KORISNIK);
        return parametri;
    }

}
