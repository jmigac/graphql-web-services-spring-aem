package io.ecx.spring.graphql.webservice.controllers.views;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import io.ecx.spring.graphql.webservice.models.Korisnik;
import io.ecx.spring.graphql.webservice.services.KorisnikServis;
import io.ecx.spring.graphql.webservice.services.MjestoServis;
import io.ecx.spring.graphql.webservice.utils.ResponseEntityUtils;

@Controller
public class KorisniciPogledKontroler {

    @Autowired
    private KorisnikServis korisnikServis;

    @Autowired
    private MjestoServis mjestoServis;

    private final ModelAndView view = new ModelAndView();

    @GetMapping("/korisnici")
    public ModelAndView dohvatiKorisniciStranicu() {
        this.view.setViewName("/korisnici");
        this.view.getModelMap().addAttribute("korisnici", this.korisnikServis.dohvatiSve());
        this.view.getModelMap().addAttribute("mjesta", this.mjestoServis.dohvatiJedinstvenaMjesta());
        return this.view;
    }

    @PostMapping("/dodajNovog")
    public ModelAndView dodajNovogKorisnika(@RequestBody String zaglavlja) {
        final String[] polja = zaglavlja.split("&");
        if (polja.length == 4) {
            final Korisnik noviKorisnik = Korisnik
                                            .builder()
                                            .ime(new String(polja[0].strip().split("=")[1].strip().getBytes(StandardCharsets.UTF_8),
                                              StandardCharsets.UTF_8))
                                            .prezime(polja[1].strip().split("=")[1].strip())
                                            .email(polja[2].strip().split("=")[1].strip().replace("%40", "@"))
                                            .mjesto(this.mjestoServis.dohvatiMjestoPoId(Long.parseLong(polja[3].strip().split("=")[1].strip())))
                                            .build();
            this.korisnikServis.spremi(noviKorisnik);
        }
        this.view.setViewName("/korisnici");
        this.view.getModelMap().addAttribute("korisnici", this.korisnikServis.dohvatiSve());
        this.view.getModelMap().addAttribute("mjesta", this.mjestoServis.dohvatiJedinstvenaMjesta());
        return this.view;
    }

}
