package com.backbase.recruitment.openbank.transactions.routes;

import com.backbase.recruitment.openbank.transactions.processors.TransactionAmountProcessor;
import com.backbase.recruitment.openbank.transactions.processors.TransactionFilterProcessor;
import com.backbase.recruitment.openbank.v2.api.Transactions;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by andresc on 06/07/2017.
 */
public class TransactionsFilterRoute extends RouteBuilder {

    @Autowired
    private TransactionFilterProcessor transactionFilterProcessor;

    @Override
    public void configure() throws Exception {

        from("direct:processTransactions")
                .policy("user")
                .to("direct:openbanktransactions")
                .unmarshal().json(JsonLibrary.Jackson, Transactions.class)
                .bean(transactionFilterProcessor).marshal().json(JsonLibrary.Jackson, String.class)
                .end();

        rest("/filter")
                .get()
                .param().name("transactionType").description("The transaction type").required(true).dataType("string").endParam()
                .responseMessage().code(500).message("Server error").endResponseMessage()
                .responseMessage().code(504).message("Server error").endResponseMessage()
                .to("direct:processTransactions");
    }
}
