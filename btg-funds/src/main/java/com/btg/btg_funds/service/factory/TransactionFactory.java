package com.btg.btg_funds.service.factory;

import com.btg.btg_funds.document.TransactionDocument;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TransactionFactory {

    public TransactionDocument createOpenTransaction(String clientId, String fundId, Double amount) {
        return create(clientId, fundId, "OPEN", amount);
    }

    public TransactionDocument createCancelTransaction(String clientId, String fundId, Double amount) {
        return create(clientId, fundId, "CANCEL", amount);
    }

    private TransactionDocument create(String clientId, String fundId, String type, Double amount) {
        TransactionDocument transaction = new TransactionDocument();
        transaction.setClientId(clientId);
        transaction.setFundId(fundId);
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setDate(LocalDateTime.now());
        return transaction;
    }
}
