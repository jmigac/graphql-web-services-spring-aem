package io.ecx.spring.graphql.webservice.controllers.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import io.ecx.spring.graphql.webservice.services.PotresServis;

@Controller
public class PotresiViewKontroler {

    @Autowired
    private PotresServis potresServis;

    private final ModelAndView view = new ModelAndView();

    @GetMapping("/potresi")
    public ModelAndView dohvatiPotresiStranicu() {
        this.view.setViewName("/potresi");
        this.view.getModelMap().addAttribute("potresi", this.potresServis.dohvatiSve());
        return this.view;
    }

}
