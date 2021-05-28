package io.ecx.spring.graphql.webservice.services;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.ecx.spring.graphql.webservice.datafetchers.PotresDataFetcher;
import io.ecx.spring.graphql.webservice.datafetchers.SviPotresiDataFetcher;
import io.ecx.spring.graphql.webservice.repository.PotresRepozitorij;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Service
public class GraphQlServis {

    @Value("classpath:shema.graphql")
    private Resource resurs;

    @Autowired
    private PotresRepozitorij repozitorij;

    @Autowired
    private SviPotresiDataFetcher sviPotresiDataFetcher;

    @Autowired
    private PotresDataFetcher potresDataFetcher;

    @Getter
    private GraphQL graphQL;

    @PostConstruct
    public void ucitavanjeSheme() throws IOException{
        final File datoteka = this.resurs.getFile();
        final TypeDefinitionRegistry registar = new SchemaParser().parse(datoteka);
        final RuntimeWiring povezanost = this.kreirajPovezanost();
        final GraphQLSchema shema = new SchemaGenerator().makeExecutableSchema(registar, povezanost);
        this.graphQL = GraphQL.newGraphQL(shema).build();
    }

    private RuntimeWiring kreirajPovezanost() {
        return RuntimeWiring
                .newRuntimeWiring()
                .type("Query", tipPovezanosti -> tipPovezanosti
                .dataFetcher("potresi", this.sviPotresiDataFetcher)
                .dataFetcher("potres", this.potresDataFetcher))
                .build();
    }



}
