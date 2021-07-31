package io.ecx.aem.web.services.core.services;

import java.util.List;

import org.apache.sling.api.resource.ResourceResolver;

import io.ecx.aem.web.services.core.models.PotresModel;
import io.ecx.aem.web.services.core.record.KorisnikZapis;

public interface RepozitorijPotresa {

    List<PotresModel> dohvatiSvePotrese(ResourceResolver resourceResolver);

    List<PotresModel> dohvatiPotresPoId(ResourceResolver resourceResolver, String id);

    void brisanjePotresa(ResourceResolver resourceResolver, String id);

    KorisnikZapis kreiranjeKorisnika(ResourceResolver resourceResolver, KorisnikZapis korisnik);

    List<PotresModel> dohvatiSvePotreseUnutarMagnitude(ResourceResolver resourceResolver, Double minMagnituda, Double maxMagnituda);

    List<PotresModel> dohvatiSvePotreseSTsunami(ResourceResolver resourceResolver);

}
