package com.btg.btg_funds.repository;

import com.btg.btg_funds.document.SubscriptionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends MongoRepository<SubscriptionDocument, String> {

    List<SubscriptionDocument> findByClientId(String clientId);

     Optional<SubscriptionDocument> findByClientIdAndFundId(String clientId, String fundId);

}
