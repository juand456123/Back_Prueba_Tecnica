package com.btg.btg_funds.notification.dispatcher;

import com.btg.btg_funds.document.ClientDocument;
import com.btg.btg_funds.document.FundDocument;
import com.btg.btg_funds.document.NotificationDocument;
import com.btg.btg_funds.notification.channel.NotificationChannel;
import com.btg.btg_funds.notification.channel.NotificationChannelRegistry;
import com.btg.btg_funds.notification.model.NotificationEventType;
import com.btg.btg_funds.notification.model.NotificationExecutionResult;
import com.btg.btg_funds.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationDispatcher {

    private final NotificationChannelRegistry channelRegistry;
    private final NotificationRepository notificationRepository;

    public NotificationDispatcher(NotificationChannelRegistry channelRegistry,
                                  NotificationRepository notificationRepository) {
        this.channelRegistry = channelRegistry;
        this.notificationRepository = notificationRepository;
    }

    public void send(ClientDocument client,
                     FundDocument fund,
                     Double amount,
                     NotificationEventType eventType) {

        NotificationChannel channel = channelRegistry.getChannel(client.getNotificationPreference());
        NotificationExecutionResult executionResult = channel.send(client, fund, amount, eventType);

        saveNotification(client, fund, eventType, executionResult);
    }

    private void saveNotification(ClientDocument client,
                                  FundDocument fund,
                                  NotificationEventType eventType,
                                  NotificationExecutionResult executionResult) {

        NotificationDocument notification = new NotificationDocument();
        notification.setClientId(client.getId());
        notification.setFundId(fund.getId());
        notification.setType(eventType.name());
        notification.setChannel(executionResult.getChannelType().name());
        notification.setDestination(executionResult.getDestination());
        notification.setMessage(executionResult.getMessage());
        notification.setStatus(executionResult.getProviderResult().isSuccess() ? "SENT" : "FAILED");
        notification.setProviderMessageId(executionResult.getProviderResult().getProviderMessageId());
        notification.setProviderStatus(executionResult.getProviderResult().getProviderStatus());
        notification.setErrorCode(executionResult.getProviderResult().getErrorCode());
        notification.setErrorMessage(executionResult.getProviderResult().getErrorMessage());
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }
}
