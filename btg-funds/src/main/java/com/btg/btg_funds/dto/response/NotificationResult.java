package com.btg.btg_funds.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResult {

    private boolean success;
    private String providerMessageId;
    private String providerStatus;
    private String errorCode;
    private String errorMessage;
}
