package com.btg.btg_funds.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "subscriptions")
public class SubscriptionDocument {

    @Id
    private String id;

    private String clientId;

    private String fundId;

    private Double amount;

}
