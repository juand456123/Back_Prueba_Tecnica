package com.btg.btg_funds.service.impl;

import com.btg.btg_funds.dto.request.CancelSubscriptionRequest;
import com.btg.btg_funds.dto.request.SubscribeRequest;
import com.btg.btg_funds.dto.response.TransactionResponse;
import com.btg.btg_funds.document.ClientDocument;
import com.btg.btg_funds.document.FundDocument;
import com.btg.btg_funds.document.SubscriptionDocument;
import com.btg.btg_funds.document.TransactionDocument;
import com.btg.btg_funds.exception.BusinessException;
import com.btg.btg_funds.exception.ResourceNotFoundException;
import com.btg.btg_funds.notification.NotificationDispatcher;
import com.btg.btg_funds.notification.NotificationEventType;
import com.btg.btg_funds.repository.ClientRepository;
import com.btg.btg_funds.repository.FundRepository;
import com.btg.btg_funds.repository.SubscriptionRepository;
import com.btg.btg_funds.repository.TransactionRepository;
import com.btg.btg_funds.service.SubscriptionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final ClientRepository clientRepository;
    private final FundRepository fundRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationDispatcher notificationDispatcher;

    public SubscriptionServiceImpl(
            ClientRepository clientRepository,
            FundRepository fundRepository,
            SubscriptionRepository subscriptionRepository,
            TransactionRepository transactionRepository,
            NotificationDispatcher notificationDispatcher) {

        this.clientRepository = clientRepository;
        this.fundRepository = fundRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.transactionRepository = transactionRepository;
        this.notificationDispatcher = notificationDispatcher;
    }

    @Override
    public void subscribe(SubscribeRequest request) {

        ClientDocument client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        FundDocument fund = fundRepository.findById(request.getFundId())
                .orElseThrow(() -> new ResourceNotFoundException("Fondo no encontrado"));

        subscriptionRepository.findByClientIdAndFundId(request.getClientId(), request.getFundId())
                .ifPresent(existing -> {
                    throw new BusinessException("El cliente ya se encuentra suscrito a este fondo");
                });

        if (client.getBalance() < request.getAmount()) {
            throw new BusinessException("No tiene saldo disponible para vincularse al fondo " + fund.getName());
        }

        if (request.getAmount() < fund.getMinimumAmount()) {
            throw new BusinessException("El monto es menor al mínimo requerido para el fondo " + fund.getName());
        }

        client.setBalance(client.getBalance() - request.getAmount());
        clientRepository.save(client);

        SubscriptionDocument subscription = new SubscriptionDocument();
        subscription.setClientId(request.getClientId());
        subscription.setFundId(request.getFundId());
        subscription.setAmount(request.getAmount());
        subscriptionRepository.save(subscription);

        TransactionDocument transaction = new TransactionDocument();
        transaction.setClientId(request.getClientId());
        transaction.setFundId(request.getFundId());
        transaction.setType("OPEN");
        transaction.setAmount(request.getAmount());
        transaction.setDate(LocalDateTime.now());
        transactionRepository.save(transaction);

        notificationDispatcher.send(client, fund, request.getAmount(), NotificationEventType.SUBSCRIPTION);
    }

    @Override
    public void cancel(CancelSubscriptionRequest request) {

        ClientDocument client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        FundDocument fund = fundRepository.findById(request.getFundId())
                .orElseThrow(() -> new ResourceNotFoundException("Fondo no encontrado"));

        SubscriptionDocument subscription = subscriptionRepository
                .findByClientIdAndFundId(request.getClientId(), request.getFundId())
                .orElseThrow(() -> new ResourceNotFoundException("Suscripción no encontrada"));

        client.setBalance(client.getBalance() + subscription.getAmount());
        clientRepository.save(client);

        TransactionDocument transaction = new TransactionDocument();
        transaction.setClientId(request.getClientId());
        transaction.setFundId(request.getFundId());
        transaction.setType("CANCEL");
        transaction.setAmount(subscription.getAmount());
        transaction.setDate(LocalDateTime.now());
        transactionRepository.save(transaction);

        subscriptionRepository.delete(subscription);

        notificationDispatcher.send(client, fund, subscription.getAmount(), NotificationEventType.CANCELLATION);
    }

    @Override
    public List<TransactionResponse> getTransactionsByClientId(String clientId) {

        clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

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
