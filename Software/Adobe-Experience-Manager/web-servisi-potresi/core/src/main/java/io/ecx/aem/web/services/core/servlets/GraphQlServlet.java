package io.ecx.aem.web.services.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import com.day.cq.search.QueryBuilder;

import graphql.ExecutionResult;
import io.ecx.aem.web.services.core.constants.RepozitorijPotresaConstants;
import io.ecx.aem.web.services.core.services.GraphQlServis;
import io.ecx.aem.web.services.core.utils.ServletPodaciSadrzajaUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=GraphQlSviPotresi",
  "sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/v1/graphql" })
public class GraphQlServlet extends SlingAllMethodsServlet {

    @Reference
    private transient ModelFactory modelFactory;

    @Reference
    private transient QueryBuilder queryBuilder;

    @Reference
    private transient GraphQlServis graphQlServis;

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            final ExecutionResult execute = this.graphQlServis.dohvatiGraphQl().execute("query{ potresi{ id magnituda naziv } }");
            final String json = this.modelFactory.exportModel(
              execute, ExporterConstants.SLING_MODEL_EXPORTER_NAME, String.class, Collections.emptyMap());
            final PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
        } catch (final RuntimeException e) {
            log.error("Error");
        } catch (MissingExporterException e) {
            log.error("Missing exporter!");
        } catch (ExportException e) {
            log.error("Missing export!");
        }
    }

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            final String upit = ServletPodaciSadrzajaUtils.dohvatiPodatkeTijela(request);
            if (StringUtils.isNotEmpty(upit)) {
                final ExecutionResult rezultat = this.graphQlServis.dohvatiGraphQl().execute(upit);
                final String json = this.modelFactory.exportModel(rezultat, ExporterConstants.SLING_MODEL_EXPORTER_NAME, String.class, Collections.emptyMap());
                final PrintWriter out = response.getWriter();
                response.setContentType(RepozitorijPotresaConstants.JSON_TIP);
                response.setContentLength(json.length());
                out.print(json);
                out.flush();
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (final RuntimeException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            log.error("Greska tokom izvodenja servleta", e);
        } catch (final MissingExporterException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            log.error("");
        } catch (final ExportException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            log.error(" ");
        }
    }

}