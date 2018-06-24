package com.backbase.recruitment.openbank.transactions.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Component;

/**
 * Created by andresc on 29/06/2017.
 */
public class TransactionsListRoute extends RouteBuilder {

    private static final Logger logger = LogManager.getLogger(TransactionsListRoute.class);

    @Override
    public void configure() throws Exception {

        rest("/transactions")
                .get()
                .description("List all the transactions for the RBS Bank")
                .consumes("application/json")
                .produces("application/json")
                .responseMessage().code(200).message("List of all transactions").endResponseMessage()
                .responseMessage().code(404).message("Resource not found").endResponseMessage()
                .responseMessage().code(500).message("Server error").endResponseMessage()
                .to("direct:openbanktransactions");

        from("direct:in").transform().simple("this is test").to("mock:out");
    }
}
