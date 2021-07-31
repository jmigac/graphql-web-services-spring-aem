package io.ecx.aem.web.services.core.services.config;

import org.apache.commons.lang.StringUtils;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "GraphQL service additional informational", description = "GraphQl configuration for setup of mandatory fields for GraphQl services.")
public @interface GraphQlServisConfig {

    @AttributeDefinition(name = "GraphQl Shema", description = "GraphQl shema for GraphQl service") String graphQlShema() default StringUtils.EMPTY;

}