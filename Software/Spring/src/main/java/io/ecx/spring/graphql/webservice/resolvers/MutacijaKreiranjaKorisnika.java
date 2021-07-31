package io.ecx.spring.graphql.webservice.resolvers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import io.ecx.spring.graphql.webservice.models.Korisnik;
import io.ecx.spring.graphql.webservice.models.Mjesto;
import io.ecx.spring.graphql.webservice.services.KorisnikServis;
import io.ecx.spring.graphql.webservice.services.MjestoServis;

@Component
public class MutacijaKreiranjaKorisnika implements DataFetcher<Korisnik> {

    @Autowired
    private MjestoServis mjestoServis;

    @Autowired
    private KorisnikServis korisnikServis;

    @Override
    public Korisnik get(final DataFetchingEnvironment dataFetchingEnvironment) {
        final Mjesto mjesto = this.mjestoServis.dohvatiMjestoPoId(Long.parseLong(dataFetchingEnvironment.getArgument("idMjesta").toString()));
        Korisnik noviKorisnik = null;
        if (mjesto != null) {
            noviKorisnik = Korisnik
                             .builder()
                             .ime(dataFetchingEnvironment.getArgument("ime"))
                             .prezime(dataFetchingEnvironment.getArgument("prezime"))
                             .email(dataFetchingEnvironment.getArgument("email"))
                             .mjesto(mjesto)
                             .build();
            this.korisnikServis.spremi(noviKorisnik);
        }
        return noviKorisnik;
    }

}
