package com.backbase.recruitment.common;

import com.fasterxml.jackson.core.JsonParseException;
import org.apache.camel.CamelAuthorizationException;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

/**
 * Created by andresc on 29/06/2017.
 */
public class ExceptionDelegateRoute extends RouteBuilder {
    public void configure() throws Exception {

        errorHandler(defaultErrorHandler()
                .maximumRedeliveries(2)
                .redeliveryDelay(1000)
                .logStackTrace(false)
                .retryAttemptedLogLevel(LoggingLevel.WARN));

        onException(JsonParseException.class)
                .handled(true)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .setBody().constant("Invalid json data")
                .log(LoggingLevel.ERROR, "ExceptionDelegateRoute", "JSON Parsing exception ${exception}")
                .end();

        onException(HttpOperationFailedException.class)
                .handled(true)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .setBody().constant("The Server is unavailable")
                .log(LoggingLevel.ERROR, "ExceptionDelegateRoute", "The Server is unavailable ${exception}")
                ;

        onException(Exception.class)
                .handled(true)
                // use HTTP status 500 when we had a server side error
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
                .setBody().constant("The Server is unavailable")
                .log(LoggingLevel.ERROR, "ExceptionDelegateRoute", "Unexpected exception ${exception}")
                .end();

        onException(CamelAuthorizationException.class)
                .handled(true)
                // use HTTP status 500 when we had a server side error
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(403))
                .setBody().constant("Access is denied")
                .log(LoggingLevel.ERROR, "ExceptionDelegateRoute", "Access is denied")
                ;

    }
}
