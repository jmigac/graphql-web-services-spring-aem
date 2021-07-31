package io.ecx.aem.web.services.core.schedulers.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Planer za periodicni preuzimatelj", description = "Planer za upravljanje periodičkim preuzimanjem potresa")
public @interface PlanerPeriodicniPreuzimateljConfig {

    @AttributeDefinition(name = "Enable job", description = "Variable to allow immediate start or stop on scheduler") boolean isJobEnabled() default true;

    @AttributeDefinition(name = "Scheduler cron expression", description = "Triggering job every day at 12pm") String scheduleExpression() default "0 */2 * ? * *";

}