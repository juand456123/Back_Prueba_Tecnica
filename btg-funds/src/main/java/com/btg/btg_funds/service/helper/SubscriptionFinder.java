package com.btg.btg_funds.service.helper;

import com.btg.btg_funds.document.ClientDocument;
import com.btg.btg_funds.document.FundDocument;
import com.btg.btg_funds.document.SubscriptionDocument;
import com.btg.btg_funds.exception.ResourceNotFoundException;
import com.btg.btg_funds.repository.ClientRepository;
import com.btg.btg_funds.repository.FundRepository;
import com.btg.btg_funds.repository.SubscriptionRepository;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionFinder {

    private final ClientRepository clientRepository;
    private final FundRepository fundRepository;
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionFinder(ClientRepository clientRepository,
                              FundRepository fundRepository,
                              SubscriptionRepository subscriptionRepository) {
        this.clientRepository = clientRepository;
        this.fundRepository = fundRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public ClientDocument findClientOrThrow(String clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
    }

    public FundDocument findFundOrThrow(String fundId) {
        return fundRepository.findById(fundId)
                .orElseThrow(() -> new ResourceNotFoundException("Fondo no encontrado"));
    }

    public SubscriptionDocument findSubscriptionOrThrow(String clientId, String fundId) {
        return subscriptionRepository.findByClientIdAndFundId(clientId, fundId)
                .orElseThrow(() -> new ResourceNotFoundException("Suscripción no encontrada"));
    }

    public boolean existsSubscription(String clientId, String fundId) {
        return subscriptionRepository.findByClientIdAndFundId(clientId, fundId).isPresent();
    }
}
