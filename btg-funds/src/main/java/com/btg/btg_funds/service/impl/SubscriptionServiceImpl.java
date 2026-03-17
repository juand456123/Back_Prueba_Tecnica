package com.btg.btg_funds.service.impl;

import com.btg.btg_funds.dto.request.CancelSubscriptionRequest;
import com.btg.btg_funds.dto.request.SubscribeRequest;
import com.btg.btg_funds.dto.response.TransactionResponse;
import com.btg.btg_funds.document.ClientDocument;
import com.btg.btg_funds.document.FundDocument;
import com.btg.btg_funds.document.SubscriptionDocument;
import com.btg.btg_funds.notification.dispatcher.NotificationDispatcher;
import com.btg.btg_funds.notification.model.NotificationEventType;
import com.btg.btg_funds.repository.ClientRepository;
import com.btg.btg_funds.repository.SubscriptionRepository;
import com.btg.btg_funds.repository.TransactionRepository;
import com.btg.btg_funds.service.SubscriptionService;
import com.btg.btg_funds.service.factory.SubscriptionFactory;
import com.btg.btg_funds.service.factory.TransactionFactory;
import com.btg.btg_funds.service.helper.SubscriptionFinder;
import com.btg.btg_funds.service.policy.SubscriptionPolicy;
import com.btg.btg_funds.service.vo.Money;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionFinder subscriptionFinder;
    private final SubscriptionRepository subscriptionRepository;
    private final ClientRepository clientRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationDispatcher notificationDispatcher;
    private final SubscriptionPolicy subscriptionPolicy;
    private final TransactionFactory transactionFactory;
    private final SubscriptionFactory subscriptionFactory;

    public SubscriptionServiceImpl(SubscriptionFinder subscriptionFinder,
                                   SubscriptionRepository subscriptionRepository,
                                   ClientRepository clientRepository,
                                   TransactionRepository transactionRepository,
                                   NotificationDispatcher notificationDispatcher,
                                   SubscriptionPolicy subscriptionPolicy,
                                   TransactionFactory transactionFactory,
                                   SubscriptionFactory subscriptionFactory) {
        this.subscriptionFinder = subscriptionFinder;
        this.subscriptionRepository = subscriptionRepository;
        this.clientRepository = clientRepository;
        this.transactionRepository = transactionRepository;
        this.notificationDispatcher = notificationDispatcher;
        this.subscriptionPolicy = subscriptionPolicy;
        this.transactionFactory = transactionFactory;
        this.subscriptionFactory = subscriptionFactory;
    }

    @Override
    public void subscribe(SubscribeRequest request) {
        ClientDocument client = subscriptionFinder.findClientOrThrow(request.getClientId());
        FundDocument fund = subscriptionFinder.findFundOrThrow(request.getFundId());
        Money subscriptionAmount = Money.positive(request.getAmount());

        subscriptionPolicy.validateNewSubscription(
                client,
                fund,
                subscriptionAmount,
                subscriptionFinder.existsSubscription(request.getClientId(), request.getFundId())
        );

        client.setBalance(
                subscriptionPolicy.calculateBalanceAfterSubscription(client, subscriptionAmount).toDouble()
        );
        clientRepository.save(client);

        subscriptionRepository.save(
                subscriptionFactory.create(request.getClientId(), request.getFundId(), subscriptionAmount)
        );

        transactionRepository.save(
                transactionFactory.createOpenTransaction(
                        request.getClientId(),
                        request.getFundId(),
                        subscriptionAmount.toDouble()
                )
        );

        notificationDispatcher.send(
                client,
                fund,
                subscriptionAmount.toDouble(),
                NotificationEventType.SUBSCRIPTION
        );
    }

    @Override
    public void cancel(CancelSubscriptionRequest request) {
        ClientDocument client = subscriptionFinder.findClientOrThrow(request.getClientId());
        FundDocument fund = subscriptionFinder.findFundOrThrow(request.getFundId());
        SubscriptionDocument subscription = subscriptionFinder.findSubscriptionOrThrow(
                request.getClientId(),
                request.getFundId()
        );

        Money subscriptionAmount = Money.positive(subscription.getAmount());

        client.setBalance(
                subscriptionPolicy.calculateBalanceAfterCancellation(client, subscriptionAmount).toDouble()
        );
        clientRepository.save(client);

        transactionRepository.save(
                transactionFactory.createCancelTransaction(
                        request.getClientId(),
                        request.getFundId(),
                        subscriptionAmount.toDouble()
                )
        );

        subscriptionRepository.delete(subscription);

        notificationDispatcher.send(
                client,
                fund,
                subscriptionAmount.toDouble(),
                NotificationEventType.CANCELLATION
        );
    }

    @Override
    public List<TransactionResponse> getTransactionsByClientId(String clientId) {
        subscriptionFinder.findClientOrThrow(clientId);

        return transactionRepository.findByClientId(clientId)
                .stream()
                .map(transaction -> new TransactionResponse(
                        transaction.getId(),
                        transaction.getClientId(),
                        transaction.getFundId(),
                        transaction.getType(),
                        transaction.getAmount(),
                        transaction.getDate()
                ))
                .collect(Collectors.toList());
    }
}
