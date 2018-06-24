package com.backbase.recruitment.openbank.transactions.processors;

import com.backbase.recruitment.openbank.v2.api.Transaction;
import com.backbase.recruitment.openbank.v2.api.Transactions;
import com.backbase.recruitment.openbank.v2.api.Value;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andresc on 10/07/2017.
 */
@Service
public class TransactionAmountProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        Transactions response = exchange.getIn().getBody(Transactions.class);
        Value value = new Value();
        double amount = 0.0;
        if (null != exchange.getIn().getHeader("transactionType")) {
            String param = String.valueOf(exchange.getIn().getHeader("transactionType"));

            for (Transaction transaction : response.getTransactions()) {
                if (param.equalsIgnoreCase(String.valueOf(transaction.getDetails().getType()))
                        && transaction.getDetails().getValue().getAmount() != null) {
                    amount += Double.parseDouble(transaction.getDetails().getValue().getAmount());
                    value.setCurrency(transaction.getDetails().getValue().getCurrency());
                }
            }
            value.setAmount(String.valueOf(amount));
        }
        exchange.getIn().setBody(value);
    }
}
