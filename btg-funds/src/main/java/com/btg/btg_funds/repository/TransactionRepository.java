package com.btg.btg_funds.repository;

import com.btg.btg_funds.document.TransactionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<TransactionDocument, String> {

    List<TransactionDocument> findByClientId(String clientId);

}
