package io.ecx.aem.web.services.core.servlets;

import java.awt.PageAttributes;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.models.factory.ExportException;
import org.apache.sling.models.factory.MissingExporterException;
import org.apache.sling.models.factory.ModelFactory;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.adobe.cq.export.json.ExporterConstants;
import com.google.common.net.MediaType;

import io.ecx.aem.web.services.core.constants.RepozitorijPotresaConstants;
import io.ecx.aem.web.services.core.models.PotresModel;
import io.ecx.aem.web.services.core.services.RepozitorijPotresa;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=DohvatiSvePotrese",
  "sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/v1/potresi/svi" })
public class RestSviPotresiServlet extends SlingAllMethodsServlet {

    @Reference
    private transient ModelFactory modelFactory;

    @Reference
    private transient RepozitorijPotresa repozitorijPotresa;

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            final PrintWriter writer = response.getWriter();
            final List<PotresModel> potresi = this.repozitorijPotresa.dohvatiSvePotrese(request.getResourceResolver());
            final String json = this.modelFactory.exportModel(potresi, ExporterConstants.SLING_MODEL_EXPORTER_NAME, String.class, Collections.emptyMap());
            response.setContentType(RepozitorijPotresaConstants.JSON_TIP);
            response.setContentLength(json.length());
            writer.print(json);
            writer.flush();
        } catch (final RuntimeException e) {
            log.error("Exception in doPost method", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (final MissingExporterException e) {
            log.error("Nedostaje izvoznik za Json datoteku", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (final ExportException e) {
            log.error("Greška prilikom izvoza Json datoteke", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}