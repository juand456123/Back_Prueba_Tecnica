package com.btg.btg_funds.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "users")
public class UserDocument {

    @Id
    private String id;

    private String username;

    private String password;

    private List<String> roles;

    private Boolean active;

    // Relación con cliente
    private String clientId;
}
