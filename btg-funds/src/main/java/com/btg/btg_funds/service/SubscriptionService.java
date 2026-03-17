package com.btg.btg_funds.service;

import java.util.List;

import com.btg.btg_funds.dto.request.CancelSubscriptionRequest;
import com.btg.btg_funds.dto.request.SubscribeRequest;
import com.btg.btg_funds.dto.response.TransactionResponse;

public interface SubscriptionService {

    void subscribe(SubscribeRequest request);

    void cancel(CancelSubscriptionRequest request);

    List<TransactionResponse> getTransactionsByClientId(String clientId);

}
