package io.ecx.spring.graphql.webservice.controllers.views;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@CrossOrigin
public class IndexPogledKontroler {

    private final ModelAndView view = new ModelAndView();

    @GetMapping
    public ModelAndView dohvatiIndexStranicu() {
        this.view.setViewName("/index");
        return this.view;
    }

}
