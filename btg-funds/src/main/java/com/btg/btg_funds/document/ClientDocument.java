package com.btg.btg_funds.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "clients")
public class ClientDocument {

    @Id
    private String id;

    private String name;

    private Double balance;

    private String notificationPreference; // EMAIL o SMS

    private String email;
    private String phone;

}