package com.btg.btg_funds.notification.model;

import com.btg.btg_funds.dto.response.NotificationResult;

public class NotificationExecutionResult {

    private final NotificationChannelType channelType;
    private final String providerName;
    private final String destination;
    private final String subject;
    private final String message;
    private final String contentType;
    private final NotificationResult providerResult;

    public NotificationExecutionResult(NotificationChannelType channelType,
                                       String providerName,
                                       String destination,
                                       String subject,
                                       String message,
                                       String contentType,
                                       NotificationResult providerResult) {
        this.channelType = channelType;
        this.providerName = providerName;
        this.destination = destination;
        this.subject = subject;
        this.message = message;
        this.contentType = contentType;
        this.providerResult = providerResult;
    }

    public NotificationChannelType getChannelType() {
        return channelType;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getDestination() {
        return destination;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public String getContentType() {
        return contentType;
    }

    public NotificationResult getProviderResult() {
        return providerResult;
    }
}
