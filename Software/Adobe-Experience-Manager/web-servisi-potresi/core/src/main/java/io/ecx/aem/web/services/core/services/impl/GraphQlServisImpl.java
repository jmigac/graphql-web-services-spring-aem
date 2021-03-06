package io.ecx.aem.web.services.core.services.impl;

import java.util.Objects;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.ecx.aem.web.services.core.datafetchers.PotresDataFetcher;
import io.ecx.aem.web.services.core.datafetchers.PotresiIzmeduMagnitudaDataFetcher;
import io.ecx.aem.web.services.core.datafetchers.SviPotresiDataFetcher;
import io.ecx.aem.web.services.core.datafetchers.TsunamiPotresiDataFetcher;
import io.ecx.aem.web.services.core.datafetchers.mutations.KreiranjeKorisnikaMutacija;
import io.ecx.aem.web.services.core.services.GraphQlServis;
import io.ecx.aem.web.services.core.services.config.GraphQlServisConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(service = GraphQlServis.class, immediate = true)
@Designate(ocd = GraphQlServisConfig.class)
public class GraphQlServisImpl implements GraphQlServis {

    @Reference
    private SviPotresiDataFetcher sviPotresiDataFetcher;

    @Reference
    private PotresiIzmeduMagnitudaDataFetcher potresiIzmeduMagnitudaDataFetcher;

    @Reference
    private TsunamiPotresiDataFetcher tsunamiPotresiDataFetcher;

    @Reference
    private PotresDataFetcher potresDataFetcher;

    @Reference
    private KreiranjeKorisnikaMutacija kreiranjeKorisnikaMutacija;

    private GraphQlServisConfig config;

    private GraphQL graphQL;

    private String graphQlShema;

    @Override
    public GraphQL dohvatiGraphQl() {
        if (Objects.isNull(this.graphQL)) {
            this.kreirajGraphQLKonfiguraciju();
        }
        return this.graphQL;
    }

    @Activate
    protected void activate(final GraphQlServisConfig config) {
        this.config = config;
        this.graphQlShema = this.config.graphQlShema();
        this.kreirajGraphQLKonfiguraciju();
        log.info("Aktivacija servisa");
    }

    @Modified
    protected void modify(final GraphQlServisConfig config) {
        this.config = config;
        this.graphQlShema = this.config.graphQlShema();
        this.kreirajGraphQLKonfiguraciju();
        log.info("Promjena vrijednost GraphQL konfiguracije");
    }

    @Deactivate
    protected void deactivate() {
        log.info("Deaktiviran GraphQL servis");
    }

    private RuntimeWiring kreirajPovezanost() {
        return RuntimeWiring
                 .newRuntimeWiring()
                 .type(
                   "Query", tipPovezanosti -> tipPovezanosti
                                                .dataFetcher("potresi", this.sviPotresiDataFetcher)
                                                .dataFetcher("potresiIzmeduMagnituda", this.potresiIzmeduMagnitudaDataFetcher)
                                                .dataFetcher("potresiSTsunamijem", this.tsunamiPotresiDataFetcher)
                                                .dataFetcher("potres", this.potresDataFetcher))
                 .type("Mutation", tipPovezanosti -> tipPovezanosti.dataFetcher("kreirajKorisnika", this.kreiranjeKorisnikaMutacija))
                 .build();
    }

    private void kreirajGraphQLKonfiguraciju() {
        final String shema = this.graphQlShema;
        final SchemaParser parser = new SchemaParser();
        final TypeDefinitionRegistry registar = parser.parse(shema);
        final RuntimeWiring povezanost = this.kreirajPovezanost();
        final GraphQLSchema shemaQl = new SchemaGenerator().makeExecutableSchema(registar, povezanost);
        this.graphQL = GraphQL.newGraphQL(shemaQl).build();
    }

}