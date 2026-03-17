package com.btg.btg_funds.notification.model;

public class NotificationMessage {

    private final String subject;
    private final String body;
    private final String contentType;

    public NotificationMessage(String subject, String body, String contentType) {
        this.subject = subject;
        this.body = body;
        this.contentType = contentType;
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
