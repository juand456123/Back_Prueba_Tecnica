package com.btg.btg_funds.service.factory;

import com.btg.btg_funds.document.SubscriptionDocument;
import com.btg.btg_funds.service.vo.Money;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionFactory {

    public SubscriptionDocument create(String clientId, String fundId, Money amount) {
        SubscriptionDocument subscription = new SubscriptionDocument();
        subscription.setClientId(clientId);
        subscription.setFundId(fundId);
        subscription.setAmount(amount.toDouble());
        return subscription;
    }
}
