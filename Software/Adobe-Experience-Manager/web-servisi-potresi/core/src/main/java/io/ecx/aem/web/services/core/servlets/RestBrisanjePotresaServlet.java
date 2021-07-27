package io.ecx.aem.web.services.core.servlets;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.models.factory.ModelFactory;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.ecx.aem.web.services.core.services.RepozitorijPotresa;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=BrisanjePotresaPoId",
  "sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/v1/potresi/brisanje" })
public class RestBrisanjePotresaServlet extends SlingAllMethodsServlet {

    @Reference
    private transient ModelFactory modelFactory;

    @Reference
    private transient RepozitorijPotresa repozitorijPotresa;

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            final Optional<String> parametar = Optional.ofNullable(request.getHeader("id"));
            if (parametar.isPresent()) {
                this.repozitorijPotresa.brisanjePotresa(request.getResourceResolver(), parametar.get());
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (final RuntimeException e) {
            log.error("Exception in doPost method", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}