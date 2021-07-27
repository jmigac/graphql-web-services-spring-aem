package io.ecx.aem.web.services.core.services;

import java.time.LocalDate;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import io.ecx.aem.web.services.core.models.PotresModel;
import io.ecx.aem.web.services.core.models.PotresOdgovor;

public interface PeriodicniPreuzimateljPotresa {

    Resource dohvatiKorjenskiDirektorij(ResourceResolver resourceResolver);

    void spremiSkupPotresa(List<PotresModel> odgovor, ResourceResolver resourceResolver, String datum);

    boolean provjeriPostojanostPotresa(LocalDate datum, ResourceResolver resourceResolver);

}
