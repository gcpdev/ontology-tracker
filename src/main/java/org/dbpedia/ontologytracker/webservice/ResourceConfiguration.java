package org.dbpedia.ontologytracker.webservice;
import org.glassfish.jersey.server.ResourceConfig;

public class ResourceConfiguration extends ResourceConfig {
    public ResourceConfiguration() {
        register(WebService.class);
    }

}