package io.ecx.spring.graphql.webservice.listeners;

import io.ecx.spring.graphql.webservice.services.PotresAPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SlusacAplikacije implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private PotresAPI apiService;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent applicationReadyEvent) {
        log.warn("Aplikacija je spremna za skidanje datoteka.");
    }
}
