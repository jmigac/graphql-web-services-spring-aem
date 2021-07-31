package io.ecx.aem.web.services.core.services.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.jackrabbit.value.StringValue;
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
                log.error("Greška za vrijeme pisanja po repozitoriju", e);
            }
        }
    }

    @Override
    public List<PotresModel> dohvatiSvePotreseUnutarMagnitude(final ResourceResolver resourceResolver, final Double minMagnituda, final Double maxMagnituda) {
        return this.dohvatiSvePotreseMagnitude(resourceResolver.adaptTo(Session.class), minMagnituda, maxMagnituda);
    }

    @Override
    public List<PotresModel> dohvatiSvePotreseSTsunami(final ResourceResolver resourceResolver) {
        return this.dohvatiPotreseSTsunamijem(resourceResolver.adaptTo(Session.class));
    }

    @Override
    public KorisnikZapis kreiranjeKorisnika(final ResourceResolver resourceResolver, final KorisnikZapis korisnik) {
        try {
            final Session sesija = resourceResolver.adaptTo(Session.class);
            final UserManager userManager = resourceResolver.adaptTo(UserManager.class);
            final Group grupa = (Group) userManager.getAuthorizable("korisnici-sadrzaja");
            final User noviKorisnik = userManager.createUser(korisnik.getKorisnickoIme(), korisnik.getLozinka());
            noviKorisnik.setProperty("./profile/familyName", new StringValue(korisnik.getIme()));
            noviKorisnik.setProperty("./profile/givenName", new StringValue(korisnik.getPrezime()));
            noviKorisnik.setProperty("./profile/aboutMe", new StringValue(korisnik.getKorisnickoIme()));
            noviKorisnik.setProperty("./profile/email", new StringValue(korisnik.getEmail()));
            grupa.addMember(userManager.getAuthorizable(korisnik.getKorisnickoIme()));
            sesija.save();
            resourceResolver.commit();
        } catch (final Exception e) {
            log.error("Greška prilikom kreiranja novog korisnika", e);
        }
        return korisnik;
    }

    private List<PotresModel> dohvatiPotreseSTsunamijem(final Session sesija) {
        Map<String, String> map = new ConcurrentHashMap<>();
        map.put(PathPredicateEvaluator.PATH, RepozitorijPotresaConstants.PUTANJA_POTRESA);
        map.put(RepozitorijPotresaConstants.PATH_SELF, Boolean.FALSE.toString());
        map.put(RepozitorijPotresaConstants.PATH_EXACT, Boolean.FALSE.toString());
        map.put(RepozitorijPotresaConstants.PRVI_PROPERTY, JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY);
        map.put(RepozitorijPotresaConstants.PRVI_PROPERTY_VRIJEDNOST, RepozitorijPotresaConstants.RESOURCE_TYPE_POTRES);
        map.put(RepozitorijPotresaConstants.DRUGI_PROPERTY, "tsunami");
        map.put(RepozitorijPotresaConstants.DRUGI_PROPERTY_OPERACIJA, "equals");
        map.put(RepozitorijPotresaConstants.PRVI_PROPERTY_VRIJEDNOST, "true");
        final Query query = this.queryBuilder.createQuery(PredicateGroup.create(map), sesija);
        final List<Resource> resursi = IteratorUtils.toList(query.getResult().getResources());
        return resursi.stream().map(resurs -> resurs.adaptTo(PotresModel.class)).collect(Collectors.toList());
    }

    private List<PotresModel> dohvatiSvePotreseMagnitude(Session sesija, Double minMagnituda, Double maxMagnituda) {
        return this
                 .dohvatiSvePotrese(sesija)
                 .stream()
                 .filter(potres -> (potres.getMagnituda() > minMagnituda && potres.getMagnituda() < maxMagnituda))
                 .collect(Collectors.toList());
    }

    private List<PotresModel> dohvatiSvePotrese(Session sesija) {
        Map<String, String> map = new ConcurrentHashMap<>();
        map.put(PathPredicateEvaluator.PATH, RepozitorijPotresaConstants.PUTANJA_POTRESA);
        map.put(RepozitorijPotresaConstants.PATH_SELF, Boolean.FALSE.toString());
        map.put(RepozitorijPotresaConstants.PATH_EXACT, Boolean.FALSE.toString());
        map.put(RepozitorijPotresaConstants.PROPERTY, JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY);
        map.put(RepozitorijPotresaConstants.PROPERTY_VALUE, RepozitorijPotresaConstants.RESOURCE_TYPE_POTRES);
        map.put(RepozitorijPotresaConstants.LIMIT, RepozitorijPotresaConstants.SVI_PODACI);
        final Query query = this.queryBuilder.createQuery(PredicateGroup.create(map), sesija);
        final List<Resource> resursi = IteratorUtils.toList(query.getResult().getResources());
        return resursi.stream().map(resurs -> resurs.adaptTo(PotresModel.class)).collect(Collectors.toList());
    }

    private List<PotresModel> dohvatiPotres(final Session sesija, final String id) {
        Map<String, String> map = new ConcurrentHashMap<>();
        map.put(PathPredicateEvaluator.PATH, RepozitorijPotresaConstants.PUTANJA_POTRESA);
        map.put(RepozitorijPotresaConstants.PATH_SELF, Boolean.FALSE.toString());
        map.put(RepozitorijPotresaConstants.PATH_EXACT, Boolean.FALSE.toString());
        map.put(RepozitorijPotresaConstants.PRVI_PROPERTY, JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY);
        map.put(RepozitorijPotresaConstants.PRVI_PROPERTY_VRIJEDNOST, RepozitorijPotresaConstants.RESOURCE_TYPE_POTRES);
        map.put(RepozitorijPotresaConstants.DRUGI_PROPERTY, RepozitorijPotresaConstants.IDENTIFIKATOR);
        map.put(RepozitorijPotresaConstants.DRUGI_PROPERTY_VRIJEDNOST, id);
        final Query query = this.queryBuilder.createQuery(PredicateGroup.create(map), sesija);
        final List<Resource> resursi = IteratorUtils.toList(query.getResult().getResources());
        return resursi.stream().map(resurs -> resurs.adaptTo(PotresModel.class)).collect(Collectors.toList());
    }

    private List<Resource> dohvatiResursPotresa(final Session sesija, final String id) {
        Map<String, String> map = new ConcurrentHashMap<>();
        map.put(PathPredicateEvaluator.PATH, RepozitorijPotresaConstants.PUTANJA_POTRESA);
        map.put(RepozitorijPotresaConstants.PATH_SELF, Boolean.FALSE.toString());
        map.put(RepozitorijPotresaConstants.PATH_EXACT, Boolean.FALSE.toString());
        map.put(RepozitorijPotresaConstants.PRVI_PROPERTY, JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY);
        map.put(RepozitorijPotresaConstants.PRVI_PROPERTY_VRIJEDNOST, RepozitorijPotresaConstants.RESOURCE_TYPE_POTRES);
        map.put(RepozitorijPotresaConstants.DRUGI_PROPERTY, RepozitorijPotresaConstants.IDENTIFIKATOR);
        map.put(RepozitorijPotresaConstants.DRUGI_PROPERTY_VRIJEDNOST, id);
        final Query query = this.queryBuilder.createQuery(PredicateGroup.create(map), sesija);
        return IteratorUtils.toList(query.getResult().getResources());
    }

}