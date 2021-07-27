package io.ecx.aem.web.services.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "GraphQL service additional informational", description = "GraphQl configuration for setup of mandatory fields for GraphQl services.")
public @interface GraphQlServisConfig {

    @AttributeDefinition(name = "GraphQl Shema", description = "GraphQl shema for GraphQl service") String graphQlShema() default "schema { query: Query, mutation: Mutation } type Query{ potresi: [Potres] potresiSTsunamijem: [Potres] potresiIzmeduMagnituda(minMagnituda: Float, maxMagnituda: Float): [Potres] potres(id: String): Potres } type Vrijeme{ temperatura: Float minimalnaTemperatura: Float maksimalnaTemperatura: Float tlak: Float vlaznostZraka: Int brzinaVjetra: Float smjerPuhanja: Int } type Lokacija{ geografskaSirina: Float geografskaDuzina: Float dubina: Float } type Potres{ id: String magnituda: Float deskriptivniOpisMjesta: String vrijemePotresa: Float url: String tsunami: Boolean tipMagnitude: String naziv: String vrijeme: Vrijeme lokacija: Lokacija mjesto: Mjesto } type Mjesto{ id: Float naziv: String drzava: String isoKod: String kontinent: String regija: String } type Korisnik{ id: Float ime: String prezime: String email: String mjesto: Mjesto } type Mutation{ kreirajKorisnika(ime: String!, prezime: String!, email: String!, idMjesta: Int!): Korisnik! }";

}