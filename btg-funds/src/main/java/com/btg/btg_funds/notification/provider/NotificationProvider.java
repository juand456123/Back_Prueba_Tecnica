package com.btg.btg_funds.notification.provider;

import com.btg.btg_funds.dto.response.NotificationResult;
import com.btg.btg_funds.notification.model.NotificationChannelType;
import com.btg.btg_funds.notification.model.NotificationRequest;

public interface NotificationProvider {

    String getProviderName();

    NotificationChannelType getChannelType();

    NotificationResult send(NotificationRequest request);
}
