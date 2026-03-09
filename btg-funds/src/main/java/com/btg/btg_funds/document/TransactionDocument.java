package com.btg.btg_funds.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import lombok.Data;

@Data
@Document(collection = "transactions")
public class TransactionDocument {

    @Id
    private String id;

    private String clientId;

    private String fundId;

    private String type; // OPEN / CANCEL

    private Double amount;

    private LocalDateTime date;

}
