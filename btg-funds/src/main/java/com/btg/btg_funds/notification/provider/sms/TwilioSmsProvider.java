package com.btg.btg_funds.notification.provider.sms;

import com.btg.btg_funds.dto.response.NotificationResult;
import com.btg.btg_funds.notification.model.NotificationChannelType;
import com.btg.btg_funds.notification.model.NotificationRequest;
import com.btg.btg_funds.notification.provider.NotificationProvider;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TwilioSmsProvider implements NotificationProvider {

    @Value("${notification.sms.provider:twilio}")
    private String providerName;

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.from-number}")
    private String fromNumber;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    @Override
    public String getProviderName() {
        return providerName.toUpperCase();
    }

    @Override
    public NotificationChannelType getChannelType() {
        return NotificationChannelType.SMS;
    }

    @Override
    public NotificationResult send(NotificationRequest request) {
        try {
            Message twilioMessage = Message.creator(
                    new PhoneNumber(request.getDestination()),
                    new PhoneNumber(fromNumber),
                    request.getBody()
            ).create();

            return new NotificationResult(
                    true,
                    twilioMessage.getSid(),
                    twilioMessage.getStatus() != null ? twilioMessage.getStatus().toString() : null,
                    twilioMessage.getErrorCode() != null ? twilioMessage.getErrorCode().toString() : null,
                    twilioMessage.getErrorMessage()
            );
        } catch (Exception e) {
            return new NotificationResult(
                    false,
                    null,
                    "FAILED",
                    null,
                    e.getMessage()
            );
        }
    }
}
