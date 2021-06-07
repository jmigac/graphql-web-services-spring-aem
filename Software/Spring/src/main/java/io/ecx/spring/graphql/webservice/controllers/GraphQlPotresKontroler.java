package io.ecx.spring.graphql.webservice.controllers;

import graphql.ExecutionResult;
import graphql.GraphQLError;
import io.ecx.spring.graphql.webservice.services.GraphQlServis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/potresi")
public class GraphQlPotresKontroler {

    @Autowired
    private GraphQlServis servis;

    @PostMapping(path = "/graphql", consumes = "application/json")
    public ResponseEntity<Object> dohvatiVrijednost(@RequestBody final String upit){
        final ExecutionResult rezultat = this.servis.getGraphQL().execute(upit);
        final List<GraphQLError> greske = rezultat.getErrors();
        greske
                .stream()
                .forEach(greska ->
                    {log.error("GraphQL greška: "+greska.getMessage());}
                );
        return new ResponseEntity<>(rezultat, HttpStatus.OK);
    }

}
