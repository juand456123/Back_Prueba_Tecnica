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
public class SmsNotificationChannel implements NotificationChannel {

    private final NotificationProviderRegistry providerRegistry;
    private final NotificationTemplateBuilder templateBuilder;
    private final String configuredProvider;

    public SmsNotificationChannel(NotificationProviderRegistry providerRegistry,
                                  NotificationTemplateBuilder templateBuilder,
                                  @Value("${notification.sms.provider:twilio}") String configuredProvider) {
        this.providerRegistry = providerRegistry;
        this.templateBuilder = templateBuilder;
        this.configuredProvider = configuredProvider;
    }

    @Override
    public NotificationChannelType getChannelType() {
        return NotificationChannelType.SMS;
    }

    @Override
    public NotificationExecutionResult send(ClientDocument client,
                                            FundDocument fund,
                                            Double amount,
                                            NotificationEventType eventType) {

        if (client.getPhone() == null || client.getPhone().isBlank()) {
            throw new BusinessException("El cliente no tiene teléfono configurado");
        }

        NotificationProvider provider = providerRegistry.getProvider(
                NotificationChannelType.SMS,
                configuredProvider
        );

        NotificationMessage message = templateBuilder.build(
                NotificationChannelType.SMS,
                eventType,
                client,
                fund,
                amount
        );

        NotificationRequest request = new NotificationRequest(
                client.getPhone(),
                message.getSubject(),
                message.getBody(),
                message.getContentType()
        );

        NotificationResult providerResult = provider.send(request);

        return new NotificationExecutionResult(
                NotificationChannelType.SMS,
                provider.getProviderName(),
                client.getPhone(),
                message.getSubject(),
                message.getBody(),
                message.getContentType(),
                providerResult
        );
    }
}
