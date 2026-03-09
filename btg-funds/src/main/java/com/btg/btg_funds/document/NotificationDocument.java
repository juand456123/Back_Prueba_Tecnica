package com.btg.btg_funds.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "notifications")
public class NotificationDocument {

    @Id
    private String id;

    private String clientId;
    private String fundId;
    private String type;
    private String channel;
    private String destination;
    private String message;
    private String status;

    private String providerMessageId;
    private String providerStatus;
    private String errorCode;
    private String errorMessage;

    private LocalDateTime createdAt;
}
