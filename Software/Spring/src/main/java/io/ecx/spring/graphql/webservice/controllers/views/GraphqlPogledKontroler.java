package io.ecx.spring.graphql.webservice.controllers.views;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import io.ecx.spring.graphql.webservice.services.GraphQlServis;

@Controller
public class GraphqlPogledKontroler {

    @Autowired
    private GraphQlServis servis;

    private static final String IDENT = "    ";
    final ModelAndView view = new ModelAndView();

    @GetMapping("/graphql-potresi")
    public ModelAndView dohvatiStranicu() {
        this.view.setViewName("/graphql-potresi");
        return this.view;
    }

    @PostMapping("/kreiraj-zahtjev")
    public ResponseEntity<Object> kreirajGraphqlZahtjev(@RequestBody final String argumenti) {
        final List<String> oznaceniElementi = Arrays.stream(argumenti.split("&"))
                                                    .map(element -> element.split("=")[0])
                                                    .collect(Collectors.toList());
        String graphqlUpit = "query{" + System.lineSeparator() + IDENT + "potresi{" + System.lineSeparator();
        for (final String argument : oznaceniElementi) {
            if (StringUtils.equals("eventId", argument)) {
                graphqlUpit = this.kreirajUpit(graphqlUpit, "idPotresa");
            } else if (StringUtils.equals("nazivPotresa", argument)) {
                graphqlUpit = this.kreirajUpit(graphqlUpit, "naziv");
            } else if (StringUtils.equals("magnitudaPotresa", argument)) {
                graphqlUpit = this.kreirajUpit(graphqlUpit, "magnituda");
            } else if (StringUtils.equals("vrijemePotresa", argument)) {
                graphqlUpit = this.kreirajUpit(graphqlUpit, "vrijemePotresa");
            } else if (StringUtils.equals("urlPotresa", argument)) {
                graphqlUpit = this.kreirajUpit(graphqlUpit, "url");
            } else if (StringUtils.equals("tsunamiPotresa", argument)) {
                graphqlUpit = this.kreirajUpit(graphqlUpit, "tsunami");
            } else if (StringUtils.equals("lokacijaPotresa", argument)) {
                graphqlUpit = this.kreirajUpit(graphqlUpit, "lokacija{");
                graphqlUpit = this.kreirajElementPodElementa(graphqlUpit, "geografskaSirina");
                graphqlUpit = this.kreirajElementPodElementa(graphqlUpit, "geografskaDuzina");
                graphqlUpit = this.kreirajElementPodElementa(graphqlUpit, "dubina");
                graphqlUpit = this.kreirajUpit(graphqlUpit, "}");
            } else if (StringUtils.equals("vrijemeOkoline", argument)) {
                graphqlUpit = this.kreirajUpit(graphqlUpit, "vrijeme{");
                graphqlUpit = this.kreirajElementPodElementa(graphqlUpit, "vlaznostZraka");
                graphqlUpit = this.kreirajElementPodElementa(graphqlUpit, "minimalnaTemperatura");
                graphqlUpit = this.kreirajElementPodElementa(graphqlUpit, "maksimalnaTemperatura");
                graphqlUpit = this.kreirajElementPodElementa(graphqlUpit, "temperatura");
                graphqlUpit = this.kreirajElementPodElementa(graphqlUpit, "brzinaVjetra");
                graphqlUpit = this.kreirajElementPodElementa(graphqlUpit, "smjerPuhanja");
                graphqlUpit = this.kreirajElementPodElementa(graphqlUpit, "tlak");
                graphqlUpit = this.kreirajUpit(graphqlUpit, "}");
            } else if (StringUtils.equals("mjestoPotresa", argument)) {
                graphqlUpit = this.kreirajUpit(graphqlUpit, "mjesto{");
                graphqlUpit = this.kreirajElementPodElementa(graphqlUpit, "naziv");
                graphqlUpit = this.kreirajElementPodElementa(graphqlUpit, "drzava");
                graphqlUpit = this.kreirajElementPodElementa(graphqlUpit, "isoKod");
                graphqlUpit = this.kreirajElementPodElementa(graphqlUpit, "kontinent");
                graphqlUpit = this.kreirajElementPodElementa(graphqlUpit, "regija");
                graphqlUpit = this.kreirajUpit(graphqlUpit, "}");
            }
        }
        graphqlUpit += IDENT + "}" + System.lineSeparator() + "}";
        return new ResponseEntity<>(this.servis.getGraphQL().execute(graphqlUpit), HttpStatus.OK);
    }

    private String kreirajUpit(String upit, final String element) {
        return StringUtils.join(upit, IDENT, IDENT, element, System.lineSeparator());
    }

    private String kreirajElementPodElementa(String upit, final String podElement) {
        return StringUtils.join(upit, IDENT, IDENT, IDENT, podElement, System.lineSeparator());
    }

}
