package com.btg.btg_funds.notification;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.btg.btg_funds.dto.response.NotificationResult;
import java.io.IOException;

@Service
public class EmailNotificationService implements NotificationService {

    @Value("${spring.sendgrid.api-key}")
    private String apiKey;

    @Value("${spring.sendgrid.from-email}")
    private String fromEmail;

    @Override
    public NotificationResult sendNotification(String destination, String message) {
         return new NotificationResult(
                    false,
                    null,
                    "",
                    null,
                    ""
            );
    
    }
    public NotificationResult sendPlainText(String email, String subject, String message) {
        return send(email, subject, message, "text/plain");
    }
    @Override
    public NotificationResult sendHtml(String email, String subject, String html) {
        return send(email, subject, html, "text/html");
    }

    private NotificationResult send(String email, String subject, String body, String contentType) {
        Email from = new Email(fromEmail);
        Email to = new Email(email);
        Content content = new Content(contentType, body);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sendGrid = new SendGrid(apiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);

            return new NotificationResult(
                    true,
                    null,
                    String.valueOf(response.getStatusCode()),
                    null,
                    null
            );

        } catch (IOException ex) {
            return new NotificationResult(
                    false,
                    null,
                    "FAILED",
                    null,
                    ex.getMessage()
            );
        }
    }
}
