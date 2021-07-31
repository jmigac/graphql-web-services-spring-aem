package io.ecx.spring.graphql.webservice.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.ecx.spring.graphql.webservice.models.KorisnickaObavijest;
import io.ecx.spring.graphql.webservice.models.Korisnik;
import io.ecx.spring.graphql.webservice.models.PotresModel;
import io.ecx.spring.graphql.webservice.repository.KorisnickaObavijestRepozitorij;

@Service
public class KorisnickaObavijestServis {

    @Autowired
    private KorisnickaObavijestRepozitorij repozitorij;

    public List<KorisnickaObavijest> dohvatiSve(){
        return this.repozitorij.findAll();
    }

    public Optional<KorisnickaObavijest> dohvati(final Long id){
        return this.repozitorij.findById(id);
    }

    public boolean postojanostObavijesti(final Korisnik korisnik, final PotresModel potres){
        return this.repozitorij.existsByPotresAndKorisnik(potres, korisnik);
    }

    public KorisnickaObavijest spremi(final KorisnickaObavijest korisnickaObavijest){
        return this.repozitorij.save(korisnickaObavijest);
    }


}
