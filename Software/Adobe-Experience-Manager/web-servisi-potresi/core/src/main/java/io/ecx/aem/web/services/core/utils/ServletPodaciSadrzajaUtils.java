package io.ecx.aem.web.services.core.utils;

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServletPodaciSadrzajaUtils {

    public static String dohvatiPodatkeTijela(final SlingHttpServletRequest zahtjev) {
        final StringBuilder buffer = new StringBuilder();
        try {
            final BufferedReader citac = zahtjev.getReader();
            String linijaCitanja = StringUtils.EMPTY;
            while ((linijaCitanja = citac.readLine()) != null) {
                buffer.append(linijaCitanja);
                buffer.append(System.lineSeparator());
            }
        } catch (final IOException e) {
            log.error("Greška prilikom čitanja tijela zahtjeva");
        }
        return buffer.toString();
    }

}
