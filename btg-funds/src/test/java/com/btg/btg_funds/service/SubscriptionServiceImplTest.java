package com.btg.btg_funds.service;

import com.btg.btg_funds.document.ClientDocument;
import com.btg.btg_funds.document.FundDocument;
import com.btg.btg_funds.document.SubscriptionDocument;
import com.btg.btg_funds.document.TransactionDocument;
import com.btg.btg_funds.dto.request.CancelSubscriptionRequest;
import com.btg.btg_funds.dto.request.SubscribeRequest;
import com.btg.btg_funds.dto.response.TransactionResponse;
import com.btg.btg_funds.exception.BusinessException;
import com.btg.btg_funds.notification.dispatcher.NotificationDispatcher;
import com.btg.btg_funds.notification.model.NotificationEventType;
import com.btg.btg_funds.repository.ClientRepository;
import com.btg.btg_funds.repository.SubscriptionRepository;
import com.btg.btg_funds.repository.TransactionRepository;
import com.btg.btg_funds.service.factory.SubscriptionFactory;
import com.btg.btg_funds.service.factory.TransactionFactory;
import com.btg.btg_funds.service.helper.SubscriptionFinder;
import com.btg.btg_funds.service.impl.SubscriptionServiceImpl;
import com.btg.btg_funds.service.policy.SubscriptionPolicy;
import com.btg.btg_funds.service.vo.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceImplTest {

    @Mock
    private SubscriptionFinder subscriptionFinder;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private NotificationDispatcher notificationDispatcher;

    @Mock
    private SubscriptionPolicy subscriptionPolicy;

    @Mock
    private TransactionFactory transactionFactory;

    @Mock
    private SubscriptionFactory subscriptionFactory;

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    private ClientDocument client;
    private FundDocument fund;
    private SubscriptionDocument subscription;
    private TransactionDocument openTransaction;
    private TransactionDocument cancelTransaction;

    @BeforeEach
    void setup() {
        client = new ClientDocument();
        client.setId("client1");
        client.setName("Juan");
        client.setBalance(500000.0);
        client.setNotificationPreference("EMAIL");
        client.setEmail("cliente@test.com");
        client.setPhone("3001234567");

        fund = new FundDocument();
        fund.setId("fund1");
        fund.setName("FPV_BTG_PACTUAL_RECAUDADORA");
        fund.setMinimumAmount(75000);
        fund.setActive(true);

        subscription = new SubscriptionDocument();
        subscription.setId("sub1");
        subscription.setClientId("client1");
        subscription.setFundId("fund1");
        subscription.setAmount(75000.0);

        openTransaction = new TransactionDocument();
        openTransaction.setId("tx-open-1");
        openTransaction.setClientId("client1");
        openTransaction.setFundId("fund1");
        openTransaction.setType("OPEN");
        openTransaction.setAmount(75000.0);
        openTransaction.setDate(LocalDateTime.now());

        cancelTransaction = new TransactionDocument();
        cancelTransaction.setId("tx-cancel-1");
        cancelTransaction.setClientId("client1");
        cancelTransaction.setFundId("fund1");
        cancelTransaction.setType("CANCEL");
        cancelTransaction.setAmount(75000.0);
        cancelTransaction.setDate(LocalDateTime.now());
    }

    @Test
    void deberiaSuscribirClienteCorrectamente() {
        SubscribeRequest request = new SubscribeRequest();
        request.setClientId("client1");
        request.setFundId("fund1");
        request.setAmount(75000.0);

        when(subscriptionFinder.findClientOrThrow("client1")).thenReturn(client);
        when(subscriptionFinder.findFundOrThrow("fund1")).thenReturn(fund);
        when(subscriptionFinder.existsSubscription("client1", "fund1")).thenReturn(false);

        when(subscriptionPolicy.calculateBalanceAfterSubscription(client, Money.positive(75000.0)))
                .thenReturn(Money.of(425000.0));

        when(subscriptionFactory.create("client1", "fund1", Money.positive(75000.0)))
                .thenReturn(subscription);

        when(transactionFactory.createOpenTransaction("client1", "fund1", 75000.0))
                .thenReturn(openTransaction);

        subscriptionService.subscribe(request);

        verify(subscriptionFinder).findClientOrThrow("client1");
        verify(subscriptionFinder).findFundOrThrow("fund1");
        verify(subscriptionFinder).existsSubscription("client1", "fund1");

        verify(subscriptionPolicy).validateNewSubscription(
                client,
                fund,
                Money.positive(75000.0),
                false
        );

        verify(subscriptionPolicy).calculateBalanceAfterSubscription(
                client,
                Money.positive(75000.0)
        );

        ArgumentCaptor<ClientDocument> clientCaptor = ArgumentCaptor.forClass(ClientDocument.class);
        verify(clientRepository).save(clientCaptor.capture());
        assertEquals(425000.0, clientCaptor.getValue().getBalance());

        verify(subscriptionFactory).create("client1", "fund1", Money.positive(75000.0));
        verify(subscriptionRepository).save(subscription);

        verify(transactionFactory).createOpenTransaction("client1", "fund1", 75000.0);
        verify(transactionRepository).save(openTransaction);

        verify(notificationDispatcher).send(
                client,
                fund,
                75000.0,
                NotificationEventType.SUBSCRIPTION
        );
    }

    @Test
    void deberiaLanzarExcepcionCuandoClienteYaEstaSuscrito() {
        SubscribeRequest request = new SubscribeRequest();
        request.setClientId("client1");
        request.setFundId("fund1");
        request.setAmount(75000.0);

        when(subscriptionFinder.findClientOrThrow("client1")).thenReturn(client);
        when(subscriptionFinder.findFundOrThrow("fund1")).thenReturn(fund);
        when(subscriptionFinder.existsSubscription("client1", "fund1")).thenReturn(true);

        doThrow(new BusinessException("El cliente ya se encuentra suscrito a este fondo"))
                .when(subscriptionPolicy)
                .validateNewSubscription(client, fund, Money.positive(75000.0), true);

        assertThrows(BusinessException.class, () -> subscriptionService.subscribe(request));

        verify(subscriptionPolicy).validateNewSubscription(
                client,
                fund,
                Money.positive(75000.0),
                true
        );

        verify(subscriptionPolicy, never()).calculateBalanceAfterSubscription(any(), any());
        verify(clientRepository, never()).save(any());
        verify(subscriptionFactory, never()).create(any(), any(), any());
        verify(subscriptionRepository, never()).save(any());
        verify(transactionFactory, never()).createOpenTransaction(any(), any(), anyDouble());
        verify(transactionRepository, never()).save(any());
        verify(notificationDispatcher, never()).send(any(), any(), any(), any());
    }

    @Test
    void deberiaLanzarExcepcionCuandoSaldoEsInsuficiente() {
        SubscribeRequest request = new SubscribeRequest();
        request.setClientId("client1");
        request.setFundId("fund1");
        request.setAmount(75000.0);

        when(subscriptionFinder.findClientOrThrow("client1")).thenReturn(client);
        when(subscriptionFinder.findFundOrThrow("fund1")).thenReturn(fund);
        when(subscriptionFinder.existsSubscription("client1", "fund1")).thenReturn(false);

        doThrow(new BusinessException("No tiene saldo disponible para vincularse al fondo"))
                .when(subscriptionPolicy)
                .validateNewSubscription(client, fund, Money.positive(75000.0), false);

        assertThrows(BusinessException.class, () -> subscriptionService.subscribe(request));

        verify(subscriptionPolicy).validateNewSubscription(
                client,
                fund,
                Money.positive(75000.0),
                false
        );

        verify(subscriptionPolicy, never()).calculateBalanceAfterSubscription(any(), any());
        verify(clientRepository, never()).save(any());
        verify(subscriptionFactory, never()).create(any(), any(), any());
        verify(subscriptionRepository, never()).save(any());
        verify(transactionFactory, never()).createOpenTransaction(any(), any(), anyDouble());
        verify(transactionRepository, never()).save(any());
        verify(notificationDispatcher, never()).send(any(), any(), any(), any());
    }

    @Test
    void deberiaCancelarSuscripcionCorrectamente() {
        CancelSubscriptionRequest request = new CancelSubscriptionRequest();
        request.setClientId("client1");
        request.setFundId("fund1");

        client.setBalance(425000.0);

        when(subscriptionFinder.findClientOrThrow("client1")).thenReturn(client);
        when(subscriptionFinder.findFundOrThrow("fund1")).thenReturn(fund);
        when(subscriptionFinder.findSubscriptionOrThrow("client1", "fund1")).thenReturn(subscription);

        when(subscriptionPolicy.calculateBalanceAfterCancellation(client, Money.positive(75000.0)))
                .thenReturn(Money.of(500000.0));

        when(transactionFactory.createCancelTransaction("client1", "fund1", 75000.0))
                .thenReturn(cancelTransaction);

        subscriptionService.cancel(request);

        verify(subscriptionFinder).findClientOrThrow("client1");
        verify(subscriptionFinder).findFundOrThrow("fund1");
        verify(subscriptionFinder).findSubscriptionOrThrow("client1", "fund1");

        verify(subscriptionPolicy).calculateBalanceAfterCancellation(
                client,
                Money.positive(75000.0)
        );

        ArgumentCaptor<ClientDocument> clientCaptor = ArgumentCaptor.forClass(ClientDocument.class);
        verify(clientRepository).save(clientCaptor.capture());
        assertEquals(500000.0, clientCaptor.getValue().getBalance());

        verify(transactionFactory).createCancelTransaction("client1", "fund1", 75000.0);
        verify(transactionRepository).save(cancelTransaction);

        verify(subscriptionRepository).delete(subscription);

        verify(notificationDispatcher).send(
                client,
                fund,
                75000.0,
                NotificationEventType.CANCELLATION
        );
    }

    @Test
    void deberiaRetornarTransaccionesDelCliente() {
        TransactionDocument transaction = new TransactionDocument();
        transaction.setId("tx1");
        transaction.setClientId("client1");
        transaction.setFundId("fund1");
        transaction.setType("OPEN");
        transaction.setAmount(75000.0);
        transaction.setDate(LocalDateTime.now());

        when(subscriptionFinder.findClientOrThrow("client1")).thenReturn(client);
        when(transactionRepository.findByClientId("client1")).thenReturn(List.of(transaction));

        List<TransactionResponse> result = subscriptionService.getTransactionsByClientId("client1");

        verify(subscriptionFinder).findClientOrThrow("client1");
        verify(transactionRepository).findByClientId("client1");

        assertEquals(1, result.size());
        assertEquals("tx1", result.get(0).getId());
        assertEquals("client1", result.get(0).getClientId());
        assertEquals("fund1", result.get(0).getFundId());
        assertEquals("OPEN", result.get(0).getType());
        assertEquals(75000.0, result.get(0).getAmount()); 
    }
}