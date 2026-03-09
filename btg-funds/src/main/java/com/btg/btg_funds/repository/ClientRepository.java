package com.btg.btg_funds.repository;

import com.btg.btg_funds.document.ClientDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientRepository extends MongoRepository<ClientDocument, String> {
}
