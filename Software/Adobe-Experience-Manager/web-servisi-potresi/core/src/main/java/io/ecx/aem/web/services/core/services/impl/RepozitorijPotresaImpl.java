package io.ecx.aem.web.services.core.services.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.jcr.Session;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.eval.PathPredicateEvaluator;

import io.ecx.aem.web.services.core.constants.RepozitorijPotresaConstants;
import io.ecx.aem.web.services.core.models.PotresModel;
import io.ecx.aem.web.services.core.record.KorisnikZapis;
import io.ecx.aem.web.services.core.services.RepozitorijPotresa;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(service = RepozitorijPotresa.class, immediate = true)
public class RepozitorijPotresaImpl implements RepozitorijPotresa {

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private QueryBuilder queryBuilder;

    @Override
    public List<PotresModel> dohvatiSvePotrese(final ResourceResolver resourceResolver) {
        return this.dohvatiSvePotrese(resourceResolver.adaptTo(Session.class));
    }

    @Override
    public List<PotresModel> dohvatiPotresPoId(final ResourceResolver resourceResolver, final String id) {
        return this.dohvatiPotres(resourceResolver.adaptTo(Session.class), id);
    }

    @Override
    public void brisanjePotresa(final ResourceResolver resourceResolver, final String id) {
        final List<Resource> potresi = this.dohvatiResursPotresa(resourceResolver.adaptTo(Session.class), id);
        if (!potresi.isEmpty()) {
            try {
                final Resource potresZaBrisanje = potresi.get(0);
                resourceResolver.delete(potresZaBrisanje);
                resourceResolver.commit();
            } catch (final PersistenceException e) {
                log.error("");
            }
        }
    }

    @Override
    public void kreiranjeKorisnika(final ResourceResolver resourceResolver, final KorisnikZapis korisnik) {

    }

    private List<PotresModel> dohvatiSvePotrese(Session sesija) {
        Map<String, String> map = new ConcurrentHashMap<>();
        map.put(PathPredicateEvaluator.PATH, RepozitorijPotresaConstants.PUTANJA_POTRESA);
        map.put(RepozitorijPotresaConstants.PATH_SELF, Boolean.FALSE.toString());
        map.put(RepozitorijPotresaConstants.PATH_EXACT, Boolean.FALSE.toString());
        map.put(RepozitorijPotresaConstants.PROPERTY, JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY);
        map.put(RepozitorijPotresaConstants.PROPERTY_VALUE, RepozitorijPotresaConstants.RESOURCE_TYPE_POTRES);
        map.put("p.limit", "-1");
        final Query query = this.queryBuilder.createQuery(PredicateGroup.create(map), sesija);
        final List<Resource> resursi = IteratorUtils.toList(query.getResult().getResources());
        return resursi.stream().map(resurs -> resurs.adaptTo(PotresModel.class)).collect(Collectors.toList());
    }

    private List<PotresModel> dohvatiPotres(final Session sesija, final String id) {
        Map<String, String> map = new ConcurrentHashMap<>();
        map.put(PathPredicateEvaluator.PATH, RepozitorijPotresaConstants.PUTANJA_POTRESA);
        map.put(RepozitorijPotresaConstants.PATH_SELF, Boolean.FALSE.toString());
        map.put(RepozitorijPotresaConstants.PATH_EXACT, Boolean.FALSE.toString());
        map.put(RepozitorijPotresaConstants.PROPERTY, JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY);
        map.put(RepozitorijPotresaConstants.PROPERTY_VALUE, RepozitorijPotresaConstants.RESOURCE_TYPE_POTRES);
        map.put(RepozitorijPotresaConstants.PROPERTY, RepozitorijPotresaConstants.IDENTIFIKATOR);
        map.put(RepozitorijPotresaConstants.PROPERTY_VALUE, id);
        final Query query = this.queryBuilder.createQuery(PredicateGroup.create(map), sesija);
        final List<Resource> resursi = IteratorUtils.toList(query.getResult().getResources());
        return resursi.stream().map(resurs -> resurs.adaptTo(PotresModel.class)).collect(Collectors.toList());
    }

    private List<Resource> dohvatiResursPotresa(final Session sesija, final String id) {
        Map<String, String> map = new ConcurrentHashMap<>();
        map.put(PathPredicateEvaluator.PATH, RepozitorijPotresaConstants.PUTANJA_POTRESA);
        map.put(RepozitorijPotresaConstants.PATH_SELF, Boolean.FALSE.toString());
        map.put(RepozitorijPotresaConstants.PATH_EXACT, Boolean.FALSE.toString());
        map.put(RepozitorijPotresaConstants.PROPERTY, JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY);
        map.put(RepozitorijPotresaConstants.PROPERTY_VALUE, RepozitorijPotresaConstants.RESOURCE_TYPE_POTRES);
        map.put(RepozitorijPotresaConstants.PROPERTY, RepozitorijPotresaConstants.IDENTIFIKATOR);
        map.put(RepozitorijPotresaConstants.PROPERTY_VALUE, id);
        final Query query = this.queryBuilder.createQuery(PredicateGroup.create(map), sesija);
        return IteratorUtils.toList(query.getResult().getResources());
    }

}