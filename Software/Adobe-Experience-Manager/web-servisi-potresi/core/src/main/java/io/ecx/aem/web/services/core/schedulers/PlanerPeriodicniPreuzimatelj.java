package io.ecx.aem.web.services.core.schedulers;

import java.util.Collections;

import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import io.ecx.aem.web.services.core.schedulers.config.PlanerPeriodicniPreuzimateljConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(service = Runnable.class, immediate = true)
@Designate(ocd = PlanerPeriodicniPreuzimateljConfig.class)
public class PlanerPeriodicniPreuzimatelj implements Runnable {

    private static final String JOB_TOPIC = "PeriodicniPreuzimatelj";

    @Reference
    private Scheduler scheduler;

    @Reference
    private JobManager jobManager;

    private String scheduleExpression;
    private boolean dozvoljenRad;

    @Override
    public void run() {
        log.info("Sending job to job consumer");
        this.jobManager.addJob(JOB_TOPIC, Collections.emptyMap());
    }

    @Activate
    private void activate(final PlanerPeriodicniPreuzimateljConfig config) {
        log.info("Planer potresa je aktiviran!");
        this.scheduleExpression = config.scheduleExpression();
        this.dozvoljenRad = config.isJobEnabled();
        this.scheduleJob();
    }

    @Modified
    public void modified(final PlanerPeriodicniPreuzimateljConfig config) {
        log.info("Planer potresa ima promjenjenu konfiguraciju!");
        this.scheduleExpression = config.scheduleExpression();
        this.dozvoljenRad = config.isJobEnabled();
        this.unScheduleJob();
        this.scheduleJob();
    }

    @Deactivate
    private void deactivate() {
        this.unScheduleJob();
    }

    private void scheduleJob() {
        try {
            if (this.dozvoljenRad) {
                log.info("Ekspresija planiranja: {}", this.scheduleExpression);
                final ScheduleOptions options = this.scheduler.EXPR(this.scheduleExpression).name(JOB_TOPIC).canRunConcurrently(false);
                this.scheduler.schedule(this, options);
            }
        } catch (final RuntimeException e) {
            log.error("Nemogućnost postavljanja posla", e);
        }
    }

    private void unScheduleJob() {
        try {
            if (this.scheduler != null) {
                log.info("Brisanje zakazanog posla: {}", JOB_TOPIC);
                this.scheduler.unschedule(JOB_TOPIC);
            }
        } catch (final RuntimeException e) {
            log.error("Nemogućnost pokretanja zakazanog posla", e);
        }
    }

}
