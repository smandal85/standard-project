package com.backbase.recruitment;

import com.fasterxml.jackson.core.JsonParseException;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.apache.camel.model.rest.RestBindingMode;

/**
 * Created by andresc on 05/07/2017.
 */
public class ConfigurationRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.auto)
                // output using pretty print
                .dataFormatProperty("prettyPrint", "true")
                // add swagger api-doc out of the box
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Backend Test OpenBank API")
                .apiProperty("api.version", "2.0.0")
                .apiProperty("api.description", "Backbase Backend Test v2.0, based on the OpenBank API project")
                // and enable CORS
                .apiProperty("cors", "true");
    }
}
