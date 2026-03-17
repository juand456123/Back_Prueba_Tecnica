package com.btg.btg_funds.notification.channel;

import com.btg.btg_funds.document.ClientDocument;
import com.btg.btg_funds.document.FundDocument;
import com.btg.btg_funds.notification.model.NotificationChannelType;
import com.btg.btg_funds.notification.model.NotificationEventType;
import com.btg.btg_funds.notification.model.NotificationExecutionResult;

public interface NotificationChannel {

    NotificationChannelType getChannelType();

    NotificationExecutionResult send(ClientDocument client,
                                     FundDocument fund,
                                     Double amount,
                                     NotificationEventType eventType);
}
