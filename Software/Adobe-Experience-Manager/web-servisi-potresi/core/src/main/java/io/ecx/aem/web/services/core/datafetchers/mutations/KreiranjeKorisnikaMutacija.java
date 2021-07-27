package io.ecx.aem.web.services.core.datafetchers.mutations;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.api.security.user.User;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import io.ecx.aem.web.services.core.record.KorisnikZapis;

public class KreiranjeKorisnikaMutacija implements DataFetcher<User> {

    @Override
    public User get(final DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        final String ime = dataFetchingEnvironment.getArgument("ime");
        final String prezime = dataFetchingEnvironment.getArgument("prezime");
        final String email = dataFetchingEnvironment.getArgument("email");
        if (StringUtils.isNoneEmpty(ime, prezime, email)) {
            final KorisnikZapis korisnik = KorisnikZapis.builder()
                                                        .ime(ime)
                                                        .prezime(prezime)
                                                        .email(email)
                                                        .build();
        }
        return null;
    }

}
