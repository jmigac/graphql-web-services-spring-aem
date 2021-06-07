package io.ecx.spring.graphql.webservice.services;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.ecx.spring.graphql.webservice.helpers.DnevnikRadaHelper;
import io.ecx.spring.graphql.webservice.models.DnevnikRada;
import io.ecx.spring.graphql.webservice.models.Mjesto;
import io.ecx.spring.graphql.webservice.repository.MjestoRepozitorij;

@Service
public class MjestoServis {

    @Autowired
    private MjestoRepozitorij repozitorij;

    @Autowired
    private DnevnikRadaServis dnevnikRadaServis;

    public Mjesto spremi(final Mjesto mjesto) {
        this.dnevnikRadaServis.spremi(DnevnikRada.builder().radnja(DnevnikRadaHelper.AKCIJA_UPISA_MJESTA_U_BAZU).vrijemeRadnje(new Date()).build());
        return this.repozitorij.save(mjesto);
    }

    public Mjesto dohvati(final String iso) {
        this.dnevnikRadaServis.spremi(DnevnikRada.builder().radnja(DnevnikRadaHelper.AKCIJA_DOHVACANJA_MJESTA).vrijemeRadnje(new Date()).build());
        return this.repozitorij.findMjestoByIsoKod(iso).get(0);
    }

    public Mjesto dohvatiMjestoPoId(final Long id) {
        this.dnevnikRadaServis.spremi(DnevnikRada.builder().radnja(DnevnikRadaHelper.AKCIJA_DOHVACANJA_MJESTA).vrijemeRadnje(new Date()).build());
        return this.repozitorij.findMjestoById(id);
    }

    public List<Mjesto> dohvatiSve() {
        this.dnevnikRadaServis.spremi(
          DnevnikRada.builder().radnja(DnevnikRadaHelper.AKCIJA_DOHVACANJA_SVIH_MJESTA).vrijemeRadnje(new Date()).build());
        return this.repozitorij.findAll();
    }

    public List<Mjesto> dohvatiJedinstvenaMjesta() {
        this.dnevnikRadaServis.spremi(
          DnevnikRada.builder().radnja(DnevnikRadaHelper.AKCIJA_DOHVACANJA_SVIH_MJESTA).vrijemeRadnje(new Date()).build());
        final Set<String> naziviDrzava = new HashSet<>();
        return this.repozitorij
                 .findAll()
                 .stream()
                 .filter(drzava -> StringUtils.isNotEmpty(drzava.getDrzava()))
                 .filter(drzava -> naziviDrzava.add(drzava.getDrzava()))
                 .collect(Collectors.toList());
    }

    public Mjesto getMjestoPoIsoRegiji(final String iso, final String regija) {
        this.dnevnikRadaServis.spremi(
          DnevnikRada.builder().radnja(DnevnikRadaHelper.AKCIJA_DOHVACANJA_MJESTA_PO_ISO).vrijemeRadnje(new Date()).build());
        return this.repozitorij.findMjestoByIsoKodAndRegija(iso, regija);
    }

}
