package com.btg.btg_funds.notification.provider.email;

import com.btg.btg_funds.dto.response.NotificationResult;
import com.btg.btg_funds.notification.model.NotificationChannelType;
import com.btg.btg_funds.notification.model.NotificationRequest;
import com.btg.btg_funds.notification.provider.NotificationProvider;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SendGridEmailProvider implements NotificationProvider {

    @Value("${notification.email.provider:sendgrid}")
    private String providerName;

    @Value("${spring.sendgrid.api-key}")
    private String apiKey;

    @Value("${spring.sendgrid.from-email}")
    private String fromEmail;

    @Override
    public String getProviderName() {
        return providerName.toUpperCase();
    }

    @Override
    public NotificationChannelType getChannelType() {
        return NotificationChannelType.EMAIL;
    }

    @Override
    public NotificationResult send(NotificationRequest request) {
        Email from = new Email(fromEmail);
        Email to = new Email(request.getDestination());
        Content content = new Content(request.getContentType(), request.getBody());
        Mail mail = new Mail(from, request.getSubject(), to, content);

        SendGrid sendGrid = new SendGrid(apiKey);
        Request sgRequest = new Request();

        try {
            sgRequest.setMethod(Method.POST);
            sgRequest.setEndpoint("mail/send");
            sgRequest.setBody(mail.build());

            Response response = sendGrid.api(sgRequest);

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
