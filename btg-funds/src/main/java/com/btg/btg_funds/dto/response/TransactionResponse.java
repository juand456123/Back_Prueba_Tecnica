package com.btg.btg_funds.dto.response;

import java.time.LocalDateTime;

public class TransactionResponse {

    private String id;
    private String clientId;
    private String fundId;
    private String type;
    private Double amount;
    private LocalDateTime date;

    public TransactionResponse() {
    }

    public TransactionResponse(String id, String clientId, String fundId, String type, Double amount, LocalDateTime date) {
        this.id = id;
        this.clientId = clientId;
        this.fundId = fundId;
        this.type = type;
        this.amount = amount;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
