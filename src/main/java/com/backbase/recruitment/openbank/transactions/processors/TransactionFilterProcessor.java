package com.backbase.recruitment.openbank.transactions.processors;

import com.backbase.recruitment.openbank.v2.api.Transaction;
import com.backbase.recruitment.openbank.v2.api.Transactions;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andresc on 10/07/2017.
 */
@Service
public class TransactionFilterProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        //Automatic marshalling by Jackson
        Transactions response = exchange.getIn().getBody(Transactions.class);
        if(null != exchange.getIn().getHeader("transactionType")) {
            String param = String.valueOf(exchange.getIn().getHeader("transactionType"));
            List<Transaction> filteredTransactions = new ArrayList<>();

            for (Transaction transaction : response.getTransactions()) {
                if (param.equalsIgnoreCase(String.valueOf(transaction.getDetails().getType()))) {
                    filteredTransactions.add(transaction);
                }
            }

            response.setTransactions(filteredTransactions);
        }
        exchange.getIn().setBody(response);
    }
}
