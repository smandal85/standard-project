package com.backbase.recruitment.openbank.common;

import com.backbase.recruitment.ConfigurationRouteBuilder;
import com.backbase.recruitment.openbank.v2.api.Transaction;
import com.backbase.recruitment.openbank.v2.api.Transactions;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.ProcessClause;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * Created by andresc on 04/07/2017.
 */
public class CommonOpenBankRoutes extends RouteBuilder {

    private static final Logger logger = LogManager.getLogger(CommonOpenBankRoutes.class);

    JacksonDataFormat format = new JacksonDataFormat(Transactions.class);

    @Override
    public void configure() throws Exception {

        from("direct:openbanktransactions")
                .routeId("OpenBankBaseTx")
                .routeDescription(
                        "Open Bank Transactions endpoint for all transactions of RBS bank.")
                .log(LoggingLevel.INFO,"Received request for : ${headers.CamelHttpUrl}")
                .errorHandler(noErrorHandler())
                .to("jetty:{{openbank.protocol}}://{{openbank.host}}/{{openbank.context}}/{{openbank.version}}/{{openbank.transactions.endpoint}}?bridgeEndpoint=true");

        from("direct:openbank")
                .routeId("OpenBankBase")
                .routeDescription(
                        "Open Bank The Royal Bank of Scotland description endpoint")
                .log(LoggingLevel.INFO, "Received request for : ${headers.CamelHttpUrl}")
                .errorHandler(noErrorHandler())
                .to("jetty:{{openbank.protocol}}://{{openbank.host}}/{{openbank.context}}/{{openbank.version}}/banks/rbs?bridgeEndpoint=true");
//                .convertBodyTo(String.class);
    }
}
