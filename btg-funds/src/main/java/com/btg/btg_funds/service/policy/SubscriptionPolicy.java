package com.btg.btg_funds.service.policy;

import com.btg.btg_funds.document.ClientDocument;
import com.btg.btg_funds.document.FundDocument;
import com.btg.btg_funds.service.vo.Money;

public interface SubscriptionPolicy {

    void validateNewSubscription(ClientDocument client,
                                 FundDocument fund,
                                 Money subscriptionAmount,
                                 boolean alreadySubscribed);

    Money calculateBalanceAfterSubscription(ClientDocument client, Money subscriptionAmount);

    Money calculateBalanceAfterCancellation(ClientDocument client, Money subscriptionAmount);
}
