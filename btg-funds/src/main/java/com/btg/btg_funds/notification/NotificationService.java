package com.btg.btg_funds.notification;

import com.btg.btg_funds.dto.response.NotificationResult;

public interface NotificationService {
   
    NotificationResult sendNotification(String destination, String message);

    NotificationResult sendHtml(String email, String subject, String html);
}
