package com.backbase.recruitment.openbank.transactions.routes;

import com.backbase.recruitment.openbank.transactions.processors.TransactionAmountProcessor;
import com.backbase.recruitment.openbank.v2.api.Transactions;
import com.backbase.recruitment.openbank.v2.api.Value;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by andresc on 10/07/2017.
 */
public class TransactionsAmountRoute extends RouteBuilder {

    @Autowired
    private TransactionAmountProcessor transactionAmountProcessor;

    @Override
    public void configure() throws Exception {

        from("direct:processAmountTransactions")
                .policy("admin")
                .to("direct:openbanktransactions")
                .unmarshal().json(JsonLibrary.Jackson, Transactions.class)
                .bean(transactionAmountProcessor).marshal().json(JsonLibrary.Jackson)
                .end();

        rest("/totalAmount")
                .get()
                .param().name("transactionType").description("The transaction type").required(true).dataType("string").endParam()
                .responseMessage().code(500).message("Server error").endResponseMessage()
                .responseMessage().code(504).message("Server error").endResponseMessage()
                .to("direct:processAmountTransactions").outType(Value.class);
    }
}
