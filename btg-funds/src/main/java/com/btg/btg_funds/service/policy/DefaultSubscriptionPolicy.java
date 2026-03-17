package com.btg.btg_funds.service.policy;

import com.btg.btg_funds.document.ClientDocument;
import com.btg.btg_funds.document.FundDocument;
import com.btg.btg_funds.exception.BusinessException;
import com.btg.btg_funds.service.vo.Money;
import org.springframework.stereotype.Component;

@Component
public class DefaultSubscriptionPolicy implements SubscriptionPolicy {

    @Override
    public void validateNewSubscription(ClientDocument client,
                                        FundDocument fund,
                                        Money subscriptionAmount,
                                        boolean alreadySubscribed) {

        if (alreadySubscribed) {
            throw new BusinessException("El cliente ya se encuentra suscrito a este fondo");
        }

        Money clientBalance = Money.of(client.getBalance());
        Money minimumAmount = Money.positive(fund.getMinimumAmount());

        subscriptionAmount.validateNotLessThan(
                minimumAmount,
                "El monto es menor al mínimo requerido para el fondo " + fund.getName()
        );

        clientBalance.validateNotLessThan(
                subscriptionAmount,
                "No tiene saldo disponible para vincularse al fondo " + fund.getName()
        );
    }

    @Override
    public Money calculateBalanceAfterSubscription(ClientDocument client, Money subscriptionAmount) {
        return Money.of(client.getBalance()).subtract(subscriptionAmount);
    }

    @Override
    public Money calculateBalanceAfterCancellation(ClientDocument client, Money subscriptionAmount) {
        return Money.of(client.getBalance()).add(subscriptionAmount);
    }
}
