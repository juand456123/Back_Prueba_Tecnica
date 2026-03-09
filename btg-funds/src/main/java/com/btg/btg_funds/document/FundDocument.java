package com.btg.btg_funds.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "funds")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundDocument {

    @Id
    private String id;

    private String name;

    private Integer minimumAmount;

    private String category;

    private Boolean active;
}
