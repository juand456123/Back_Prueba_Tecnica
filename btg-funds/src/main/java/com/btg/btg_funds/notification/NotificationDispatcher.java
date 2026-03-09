package com.btg.btg_funds.notification;

import com.btg.btg_funds.document.ClientDocument;
import com.btg.btg_funds.document.FundDocument;
import com.btg.btg_funds.document.NotificationDocument;
import com.btg.btg_funds.exception.BusinessException;
import com.btg.btg_funds.notification.template.NotificationTemplateBuilder;
import com.btg.btg_funds.repository.NotificationRepository;
import com.btg.btg_funds.dto.response.NotificationResult;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class NotificationDispatcher {

    private final NotificationService emailNotificationService;
    private final NotificationService smsNotificationService;
    private final NotificationTemplateBuilder templateBuilder;
    private final NotificationRepository notificationRepository;

    public NotificationDispatcher(
            @Qualifier("emailNotificationService") NotificationService emailNotificationService,
            @Qualifier("twilioSmsService") NotificationService smsNotificationService,
            NotificationTemplateBuilder templateBuilder,
            NotificationRepository notificationRepository) {
        this.emailNotificationService = emailNotificationService;
        this.smsNotificationService = smsNotificationService;
        this.templateBuilder = templateBuilder;
        this.notificationRepository = notificationRepository;
    }

    public void send(ClientDocument client,
                     FundDocument fund,
                     Double amount,
                     NotificationEventType eventType) {

        String preference = client.getNotificationPreference();

        if (preference == null || preference.isBlank()) {
            throw new BusinessException("El cliente no tiene preferencia de notificación configurada");
        }

        if ("EMAIL".equalsIgnoreCase(preference)) {
            if (client.getEmail() == null || client.getEmail().isBlank()) {
                throw new BusinessException("El cliente no tiene email configurado");
            }

            String subject = templateBuilder.buildSubject(eventType);
            String html = templateBuilder.buildHtmlMessage(eventType, client, fund, amount);

            NotificationResult result = emailNotificationService.sendHtml(client.getEmail(), subject, html);

            saveNotification(client, fund, eventType, "EMAIL", client.getEmail(), html, result);
            return;
        }

        if ("SMS".equalsIgnoreCase(preference)) {
            if (client.getPhone() == null || client.getPhone().isBlank()) {
                throw new BusinessException("El cliente no tiene teléfono configurado");
            }

            String plainMessage = templateBuilder.buildPlainMessage(eventType, client, fund, amount);

            NotificationResult result = smsNotificationService.sendNotification(client.getPhone(), plainMessage);

            saveNotification(client, fund, eventType, "SMS", client.getPhone(), plainMessage, result);
            return;
        }

        throw new BusinessException("Preferencia de notificación no válida para el cliente");
    }

    private void saveNotification(ClientDocument client,
                                  FundDocument fund,
                                  NotificationEventType eventType,
                                  String channel,
                                  String destination,
                                  String message,
                                  NotificationResult result) {

        NotificationDocument notification = new NotificationDocument();
        notification.setClientId(client.getId());
        notification.setFundId(fund.getId());
        notification.setType(eventType.name());
        notification.setChannel(channel);
        notification.setDestination(destination);
        notification.setMessage(message);
        notification.setStatus(result.isSuccess() ? "SENT" : "FAILED");
        notification.setProviderMessageId(result.getProviderMessageId());
        notification.setProviderStatus(result.getProviderStatus());
        notification.setErrorCode(result.getErrorCode());
        notification.setErrorMessage(result.getErrorMessage());
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }
}
