package com.btg.btg_funds.notification.channel;

import com.btg.btg_funds.document.ClientDocument;
import com.btg.btg_funds.document.FundDocument;
import com.btg.btg_funds.dto.response.NotificationResult;
import com.btg.btg_funds.exception.BusinessException;
import com.btg.btg_funds.notification.model.NotificationChannelType;
import com.btg.btg_funds.notification.model.NotificationEventType;
import com.btg.btg_funds.notification.model.NotificationExecutionResult;
import com.btg.btg_funds.notification.model.NotificationMessage;
import com.btg.btg_funds.notification.model.NotificationRequest;
import com.btg.btg_funds.notification.provider.NotificationProvider;
import com.btg.btg_funds.notification.provider.NotificationProviderRegistry;
import com.btg.btg_funds.notification.template.NotificationTemplateBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationChannel implements NotificationChannel {

    private final NotificationProviderRegistry providerRegistry;
    private final NotificationTemplateBuilder templateBuilder;
    private final String configuredProvider;

    public EmailNotificationChannel(NotificationProviderRegistry providerRegistry,
                                    NotificationTemplateBuilder templateBuilder,
                                    @Value("${notification.email.provider:sendgrid}") String configuredProvider) {
        this.providerRegistry = providerRegistry;
        this.templateBuilder = templateBuilder;
        this.configuredProvider = configuredProvider;
    }

    @Override
    public NotificationChannelType getChannelType() {
        return NotificationChannelType.EMAIL;
    }

    @Override
    public NotificationExecutionResult send(ClientDocument client,
                                            FundDocument fund,
                                            Double amount,
                                            NotificationEventType eventType) {

        if (client.getEmail() == null || client.getEmail().isBlank()) {
            throw new BusinessException("El cliente no tiene email configurado");
        }

        NotificationProvider provider = providerRegistry.getProvider(
                NotificationChannelType.EMAIL,
                configuredProvider
        );

        NotificationMessage message = templateBuilder.build(
                NotificationChannelType.EMAIL,
                eventType,
                client,
                fund,
                amount
        );

        NotificationRequest request = new NotificationRequest(
                client.getEmail(),
                message.getSubject(),
                message.getBody(),
                message.getContentType()
        );

        NotificationResult providerResult = provider.send(request);

        return new NotificationExecutionResult(
                NotificationChannelType.EMAIL,
                provider.getProviderName(),
                client.getEmail(),
                message.getSubject(),
                message.getBody(),
                message.getContentType(),
                providerResult
        );
    }
}