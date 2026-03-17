package com.btg.btg_funds.notification.model;

public class NotificationRequest {

    private final String destination;
    private final String subject;
    private final String body;
    private final String contentType;

    public NotificationRequest(String destination, String subject, String body, String contentType) {
        this.destination = destination;
        this.subject = subject;
        this.body = body;
        this.contentType = contentType;
    }

    public String getDestination() {
        return destination;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getContentType() {
        return contentType;
    }
}
