package com.btg.btg_funds.dto.request;

import lombok.Data;

@Data
public class SubscribeRequest {

    private String clientId;
    private String fundId;
    private Double amount;
}
