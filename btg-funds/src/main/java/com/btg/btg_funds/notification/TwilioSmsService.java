package com.btg.btg_funds.notification;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.btg.btg_funds.dto.response.NotificationResult;

@Service
public class TwilioSmsService implements NotificationService {

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
    public NotificationResult sendNotification(String destination, String message) {

        try {
            Message twilioMessage = Message.creator(
                    new PhoneNumber(destination),
                    new PhoneNumber(fromNumber),
                    message
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

    public NotificationResult sendHtml(String email, String subject, String html) {
        return new NotificationResult(
                    false,
                    null,
                    "",
                    null,
                    ""
            );
    }
    
}
