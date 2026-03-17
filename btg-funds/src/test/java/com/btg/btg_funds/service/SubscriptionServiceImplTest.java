package com.btg.btg_funds.service;

import com.btg.btg_funds.document.ClientDocument;
import com.btg.btg_funds.document.FundDocument;
import com.btg.btg_funds.document.SubscriptionDocument;
import com.btg.btg_funds.dto.request.CancelSubscriptionRequest;
import com.btg.btg_funds.dto.request.SubscribeRequest;
import com.btg.btg_funds.exception.BusinessException;
import com.btg.btg_funds.notification.dispatcher.NotificationDispatcher;
import com.btg.btg_funds.notification.model.NotificationEventType;
import com.btg.btg_funds.repository.ClientRepository;
import com.btg.btg_funds.repository.FundRepository;
import com.btg.btg_funds.repository.SubscriptionRepository;
import com.btg.btg_funds.repository.TransactionRepository;
import com.btg.btg_funds.service.impl.SubscriptionServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private FundRepository fundRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private NotificationDispatcher notificationDispatcher;

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    private ClientDocument client;
    private FundDocument fund;

    @BeforeEach
    void setup() {

        client = new ClientDocument();
        client.setId("client1");
        client.setBalance(500000.0);
        client.setNotificationPreference("EMAIL");
        client.setEmail("cliente@test.com");

        fund = new FundDocument();
        fund.setId("fund1");
        fund.setName("FPV_BTG_PACTUAL_RECAUDADORA");
        fund.setMinimumAmount(75000);
        fund.setActive(true);
    }

    @Test
    void deberiaSuscribirClienteCorrectamente() {

        SubscribeRequest request = new SubscribeRequest();
        request.setClientId("client1");
        request.setFundId("fund1");
        request.setAmount(75000.0);

        when(clientRepository.findById("client1")).thenReturn(Optional.of(client));
        when(fundRepository.findById("fund1")).thenReturn(Optional.of(fund));
        when(subscriptionRepository.findByClientIdAndFundId("client1", "fund1"))
                .thenReturn(Optional.empty());

        subscriptionService.subscribe(request);

        verify(subscriptionRepository, times(1)).save(any(SubscriptionDocument.class));
        verify(transactionRepository, times(1)).save(any());
        verify(clientRepository, times(1)).save(any());

        verify(notificationDispatcher, times(1))
                .send(client, fund, 75000.0, NotificationEventType.SUBSCRIPTION);
    }

    @Test
    void deberiaLanzarExcepcionCuandoClienteYaEstaSuscrito() {

        SubscribeRequest request = new SubscribeRequest();
        request.setClientId("client1");
        request.setFundId("fund1");
        request.setAmount(75000.0);

        SubscriptionDocument existingSubscription = new SubscriptionDocument();
        existingSubscription.setId("sub1");
        existingSubscription.setClientId("client1");
        existingSubscription.setFundId("fund1");
        existingSubscription.setAmount(75000.0);

        when(clientRepository.findById("client1")).thenReturn(Optional.of(client));
        when(fundRepository.findById("fund1")).thenReturn(Optional.of(fund));
        when(subscriptionRepository.findByClientIdAndFundId("client1", "fund1"))
                .thenReturn(Optional.of(existingSubscription));

        assertThrows(BusinessException.class, () -> subscriptionService.subscribe(request));

        verify(subscriptionRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
        verify(clientRepository, never()).save(any());
        verify(notificationDispatcher, never()).send(any(), any(), any(), any());
    }

    @Test
    void deberiaLanzarExcepcionCuandoSaldoEsInsuficiente() {

        SubscribeRequest request = new SubscribeRequest();
        request.setClientId("client1");
        request.setFundId("fund1");
        request.setAmount(75000.0);

        client.setBalance(10000.0);

        when(clientRepository.findById("client1")).thenReturn(Optional.of(client));
        when(fundRepository.findById("fund1")).thenReturn(Optional.of(fund));
        when(subscriptionRepository.findByClientIdAndFundId("client1", "fund1"))
                .thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> subscriptionService.subscribe(request));

        verify(subscriptionRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
        verify(clientRepository, never()).save(any());
        verify(notificationDispatcher, never()).send(any(), any(), any(), any());
    }

    @Test
    void deberiaCancelarSuscripcionCorrectamente() {

        CancelSubscriptionRequest request = new CancelSubscriptionRequest();
        request.setClientId("client1");
        request.setFundId("fund1");

        SubscriptionDocument subscription = new SubscriptionDocument();
        subscription.setId("sub1");
        subscription.setClientId("client1");
        subscription.setFundId("fund1");
        subscription.setAmount(75000.0);

        client.setBalance(425000.0);

        when(clientRepository.findById("client1")).thenReturn(Optional.of(client));
        when(fundRepository.findById("fund1")).thenReturn(Optional.of(fund));
        when(subscriptionRepository.findByClientIdAndFundId("client1", "fund1"))
                .thenReturn(Optional.of(subscription));

        subscriptionService.cancel(request);

        verify(subscriptionRepository, times(1)).delete(subscription);
        verify(transactionRepository, times(1)).save(any());
        verify(clientRepository, times(1)).save(any());

        verify(notificationDispatcher, times(1))
                .send(client, fund, 75000.0, NotificationEventType.CANCELLATION);
    }

    @Test
    void deberiaRetornarTransaccionesDelCliente() {
        when(clientRepository.findById("client1")).thenReturn(Optional.of(client));
        when(transactionRepository.findByClientId("client1")).thenReturn(List.of());

        subscriptionService.getTransactionsByClientId("client1");

        verify(clientRepository, times(1)).findById("client1");
        verify(transactionRepository, times(1)).findByClientId("client1");
    }

}
