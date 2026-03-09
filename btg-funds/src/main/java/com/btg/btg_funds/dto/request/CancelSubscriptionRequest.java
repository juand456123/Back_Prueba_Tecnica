package com.btg.btg_funds.dto.request;

public class CancelSubscriptionRequest {

    private String clientId;
    private String fundId;

    public CancelSubscriptionRequest() {
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }
}
